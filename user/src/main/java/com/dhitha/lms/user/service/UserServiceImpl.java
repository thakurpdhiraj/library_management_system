package com.dhitha.lms.user.service;

import com.dhitha.lms.user.dto.UserDTO;
import com.dhitha.lms.user.entity.User;
import com.dhitha.lms.user.error.GenericException;
import com.dhitha.lms.user.error.UserNotFoundException;
import com.dhitha.lms.user.repository.UserRepository;
import java.beans.FeatureDescriptor;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Implementation class for {@link UserService}
 *
 * @author Dhiraj
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final ModelMapper modelMapper;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDTO findByCredentials(String username, String password) throws UserNotFoundException {
    Assert.notNull(username, "'username' parameter cannot be null");
    Assert.notNull(password, "'password' parameter cannot be null");
    return userRepository
        .findByUsername(username)
        .filter(u -> passwordEncoder.matches(password, u.getPassword()))
        .map(this::mapToDTO)
        .orElseThrow(() -> new UserNotFoundException("Invalid username / password"));
  }

  @Override
  public List<UserDTO> findAll() {
    List<User> users = userRepository.findAll();
    return users.stream().map(this::mapToDTO).collect(Collectors.toList());
  }

  @Override
  public UserDTO findById(Long id) throws UserNotFoundException {
    return userRepository
        .findById(id)
        .map(this::mapToDTO)
        .orElseThrow(() -> new UserNotFoundException("User Not found with id: " + id));
  }

  @Override
  public UserDTO findByUserName(String username) throws UserNotFoundException {
    return userRepository
        .findByUsername(username)
        .map(this::mapToDTO)
        .orElseThrow(() -> new UserNotFoundException("Invalid username: " + username));
  }

  @Override
  public UserDTO save(UserDTO userDTO) throws GenericException {
    userDTO.setId(null);
    userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    try {
      User user = userRepository.saveAndFlush(mapToEntity(userDTO));
      return userRepository
          .findById(user.getId())
          .map(this::mapToDTO)
          .orElseThrow(
              () ->
                  new GenericException(
                      "Error in saving user ", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    } catch (DataIntegrityViolationException cx) {
      log.error("Error creating new user with details {}", userDTO, cx);
      throw new GenericException(
          "User with username " + userDTO.getUsername() + " already exists.",
          HttpStatus.BAD_REQUEST.value());
    }
  }

  @Override
  public UserDTO update(UserDTO userDTO) throws UserNotFoundException {
    if (!StringUtils.isEmpty(userDTO.getPassword())) {
      userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    }
    UserDTO existingUserDTO = this.findById(userDTO.getId());
    BeanWrapper bw = new BeanWrapperImpl(userDTO);
    BeanUtils.copyProperties(
        userDTO,
        existingUserDTO,
        Arrays.stream(bw.getPropertyDescriptors())
            .filter(pd -> bw.getPropertyValue(pd.getName()) == null)
            .map(FeatureDescriptor::getName)
            .toArray(String[]::new));
    User user = userRepository.saveAndFlush(mapToEntity(existingUserDTO));
    return mapToDTO(user);
  }

  @Override
  public void delete(Long id) throws UserNotFoundException {
    try {
      userRepository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new UserNotFoundException("User Not found with id: " + id);
    }
  }

  private UserDTO mapToDTO(User user) {
    return modelMapper.map(user, UserDTO.class);
  }

  private User mapToEntity(UserDTO userDTO) {
    return modelMapper.map(userDTO, User.class);
  }
}
