package com.dhitha.lms.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dhitha.lms.user.dto.UserDTO;
import com.dhitha.lms.user.entity.User;
import com.dhitha.lms.user.error.GenericException;
import com.dhitha.lms.user.error.UserNotFoundException;
import com.dhitha.lms.user.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Unit tests for {@link UserService}
 *
 * @author Dhiraj
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  private static final String ENCODED_PASS =
      "$2y$10$NzzC81zVp.Wn6WvtCMW/t.dg4tbvErIUzAwwdy0uC4PgycdeUAw0K";

  private final ModelMapper modelMapper = new ModelMapper();

  @Spy private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Mock private UserRepository userRepositoryMock;

  private UserService subject;

  @BeforeEach
  void init() {
    subject = new UserServiceImpl(userRepositoryMock, modelMapper, passwordEncoder);
  }

  /* ********************** findByCredentials ************************** */
  @Test
  @DisplayName("findByCredentials: valid input happy flow, expected success")
  void testFindByCredentials() throws Exception {
    when(userRepositoryMock.findByUsername("username"))
        .thenReturn(createMockOptionalUser(1L, "user"));
    UserDTO result = subject.findByCredentials("username", "pass");
    assertEquals("user", result.getName());
    assertEquals("username", result.getUsername());
    assertEquals("email", result.getEmail());
    verify(userRepositoryMock).findByUsername("username");
    verify(passwordEncoder).matches("pass", ENCODED_PASS);
  }

  @Test
  @DisplayName("findByCredentials: unknown username, expected UserNotFoundException")
  void testFindByCredentialsThrowsUserNotFoundException() {
    assertThrows(
        UserNotFoundException.class,
        () -> {
          when(userRepositoryMock.findByUsername("username")).thenReturn(Optional.empty());
          subject.findByCredentials("username", "passed");
        });
    verify(userRepositoryMock).findByUsername("username");
    verify(passwordEncoder, never()).matches(anyString(), anyString());
  }

  @Test
  @DisplayName("findByCredentials: wrong password, expected UserNotFoundException")
  void testFindByCredentialsIncorrectPasswordThrowsUserNotFoundException() {
    assertThrows(
        UserNotFoundException.class,
        () -> {
          when(userRepositoryMock.findByUsername("username"))
              .thenReturn(createMockOptionalUser(1L, "name"));
          subject.findByCredentials("username", "passed");
        });
    verify(userRepositoryMock).findByUsername("username");
    verify(passwordEncoder).matches("passed", ENCODED_PASS);
  }

  @Test
  @DisplayName("findByCredentials: null username, expected IllegalArgumentException")
  void testFindByCredentialsNullUsername() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          subject.findByCredentials(null, "passed");
        });
    verify(userRepositoryMock, never()).findByUsername(anyString());
    verify(passwordEncoder, never()).matches(anyString(), anyString());
  }

  @Test
  @DisplayName("findByCredentials: null password, expected IllegalArgumentException")
  void testFindByCredentialsNullPassword() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          subject.findByCredentials("username", null);
        });
    verify(userRepositoryMock, never()).findByUsername(anyString());
    verify(passwordEncoder, never()).matches(anyString(), anyString());
  }

  /* ********************** findAll ************************** */

  @Test
  @DisplayName("findAll: valid input happy flow, expected success")
  void testFindAll() {
    UserDTO mockDTO =
        UserDTO.builder()
            .id(1L)
            .name("user")
            .username("username")
            .password(ENCODED_PASS)
            .accountNonExpired(true)
            .accountNonLocked(true)
            .enabled(true)
            .credentialsNonExpired(true)
            .email("email")
            .build();
    when(userRepositoryMock.findAll())
        .thenReturn(Collections.singletonList(createMockUser(1L, "user")));
    List<UserDTO> result = subject.findAll();
    assertThat(result).hasSize(1).contains(mockDTO);
    verify(userRepositoryMock).findAll();
  }

  /* ********************** findById ************************** */

  @Test
  @DisplayName("findById: valid input happy flow, expected success")
  void testFindById() throws Exception {
    when(userRepositoryMock.findById(1L)).thenReturn(createMockOptionalUser(1L, "user"));
    UserDTO result = subject.findById(1L);
    assertNotNull(result);
    assertEquals("user", result.getName());
    assertEquals("username", result.getUsername());
    assertEquals("email", result.getEmail());
    assertTrue(result.getEnabled());
    assertTrue(result.getAccountNonExpired());
    assertTrue(result.getAccountNonLocked());
    assertTrue(result.getCredentialsNonExpired());
    assertTrue(passwordEncoder.matches("pass", result.getPassword()));
    verify(userRepositoryMock).findById(1L);
  }

  @Test
  @DisplayName("findById: unknown user id, expected UserNotFoundException")
  void testFindByIdThrowsUserNotFoundException() {
    assertThrows(
        UserNotFoundException.class,
        () -> {
          when(userRepositoryMock.findById(1L)).thenReturn(Optional.empty());
          subject.findById(1L);
        });
    verify(userRepositoryMock).findById(1L);
  }

  /* ********************** findByUserName ************************** */
  @Test
  @DisplayName("findByUsername: valid input happy flow, expected success")
  void testFindByUsername() throws Exception {
    when(userRepositoryMock.findByUsername("username"))
        .thenReturn(createMockOptionalUser(1L, "user"));
    UserDTO result = subject.findByUserName("username");
    assertNotNull(result);
    assertEquals("user", result.getName());
    assertEquals("username", result.getUsername());
    assertEquals("email", result.getEmail());
    assertTrue(result.getEnabled());
    assertTrue(result.getAccountNonExpired());
    assertTrue(result.getAccountNonLocked());
    assertTrue(result.getCredentialsNonExpired());
    assertTrue(passwordEncoder.matches("pass", result.getPassword()));
    verify(userRepositoryMock).findByUsername("username");
  }

  @Test
  @DisplayName("findByUsername: unknown username, expected UserNotFoundException")
  void testFindByUsernameThrowsUserNotFoundException() {
    assertThrows(
        UserNotFoundException.class,
        () -> {
          when(userRepositoryMock.findByUsername("username")).thenReturn(Optional.empty());
          subject.findByUserName("username");
        });
    verify(userRepositoryMock).findByUsername("username");
  }

  /* ********************** save ************************** */
  @Test
  @DisplayName("save: valid input happy flow, expected success")
  void testSave() throws Exception {
    UserDTO userMock =
        UserDTO.builder().username("username").name("name").email("email").password("pass").build();
    when(userRepositoryMock.saveAndFlush(any(User.class))).thenReturn(createMockUser(1L, "name"));
    when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(createMockUser(1L, "name")));
    UserDTO result = subject.save(userMock);
    verify(passwordEncoder).encode("pass");
    verify(userRepositoryMock).saveAndFlush(any(User.class));
    verify(userRepositoryMock).findById(1L);
    assertEquals(1L, result.getId());
  }

  @Test
  @DisplayName("save: username already exists in db, expected GenericException")
  void testSaveWithExistingUsername() {
    assertThrows(
        GenericException.class,
        () -> {
          UserDTO userMock =
              UserDTO.builder()
                  .username("username")
                  .name("name")
                  .email("email")
                  .password("pass")
                  .build();
          when(userRepositoryMock.saveAndFlush(any(User.class)))
              .thenThrow(DataIntegrityViolationException.class);
          subject.save(userMock);
        });
    verify(passwordEncoder).encode("pass");
    verify(userRepositoryMock).saveAndFlush(any(User.class));
    verify(userRepositoryMock, never()).findById(1L);
  }

  /* ********************** update ************************** */
  @Test
  @DisplayName("update: null password, expected password encoder should not encode null password")
  void testUpdateNotEncodingNullPassword() throws Exception {
    UserDTO userMock = UserDTO.builder().id(1L).name("update").build();
    when(userRepositoryMock.findById(1L)).thenReturn(createMockOptionalUser(1L, "user"));
    when(userRepositoryMock.saveAndFlush(any(User.class))).thenReturn(createMockUser(1L, "update"));
    subject.update(userMock);
    verify(passwordEncoder, never()).encode(anyString());
  }

  @Test
  @DisplayName("update: valid password, expected password encoder should encode password")
  void testUpdateEncodingPassword() throws Exception {
    UserDTO userMock = UserDTO.builder().id(1L).name("update").password("password").build();
    when(userRepositoryMock.findById(1L)).thenReturn(createMockOptionalUser(1L, "user"));
    when(userRepositoryMock.saveAndFlush(any(User.class))).thenReturn(createMockUser(1L, "update"));
    subject.update(userMock);
    verify(passwordEncoder).encode(anyString());
  }

  @Test
  @DisplayName("update: unknown user id, expected UserNotFoundException")
  void testUpdateThrowsUserNotFoundException() throws Exception {
    assertThrows(
        UserNotFoundException.class,
        () -> {
          when(userRepositoryMock.findById(1L)).thenReturn(Optional.empty());
          subject.update(UserDTO.builder().id(1L).build());
        });
    verify(userRepositoryMock, never()).saveAndFlush(any(User.class));
  }

  /* ********************** delete ************************** */

  @Test
  @DisplayName("delete: valid flow, expected happy scenario")
  void testDelete() throws Exception {
    doNothing().when(userRepositoryMock).deleteById(2L);
    subject.delete(2L);
  }

  @Test
  @DisplayName("delete: unknown user id, expected UserNotFoundException")
  void testDeleteThrowsUserNotFoundException() throws Exception {
    assertThrows(
        UserNotFoundException.class,
        () -> {
          doThrow(EmptyResultDataAccessException.class).when(userRepositoryMock).deleteById(1L);
          subject.delete(1L);
        });
    verify(userRepositoryMock).deleteById(anyLong());
  }

  /* ********************** Utility ************************** */
  private Optional<User> createMockOptionalUser(Long id, String name) {
    return Optional.of(createMockUser(id, name));
  }

  private User createMockUser(Long id, String name) {
    return User.builder()
        .id(id)
        .name(name)
        .username("username")
        .password(ENCODED_PASS) // pass
        .accountNonExpired(true)
        .accountNonLocked(true)
        .enabled(true)
        .credentialsNonExpired(true)
        .email("email")
        .build();
  }
}
