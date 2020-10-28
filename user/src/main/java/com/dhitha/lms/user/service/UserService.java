package com.dhitha.lms.user.service;

import com.dhitha.lms.user.dto.UserDTO;
import com.dhitha.lms.user.error.GenericException;
import com.dhitha.lms.user.error.UserNotFoundException;
import java.util.List;

/**
 * Service class for {@link UserDTO}
 *
 * @author Dhiraj
 */
public interface UserService {

  /**
   * Find and validate the user's credentials
   * @param username username of user
   * @param password raw password to validate
   * @return user matching the credentials
   * @throws UserNotFoundException if no user exists or credentials are incorrect
   */
  UserDTO findByCredentials(String username,String password) throws UserNotFoundException;

  /**
   * Find all users in LMS
   * @return list of users
   */
  List<UserDTO> findAll();

  /**
   * Find a user by id
   *
   * @param id unique identifier
   * @return user corresponding to the id
   * @throws UserNotFoundException if no user exists with id
   */
  UserDTO findById(Long id) throws UserNotFoundException;

  /**
   * Find a user by id
   *
   * @param username unique identifier
   * @return user corresponding to the username
   * @throws UserNotFoundException if no user exists with username
   */
  UserDTO findByUserName(String username) throws UserNotFoundException;

  /**
   * Save a user
   *
   * @param user user to save
   * @return saved user with new id
   */
  UserDTO save(UserDTO user) throws GenericException;

  /**
   * Update a user
   *
   * @param user user to update with null properties ignored
   * @return updated user
   * @throws UserNotFoundException if no user exists with id of passed user
   */
  UserDTO update(UserDTO user) throws UserNotFoundException;

  /**
   * Delete a user
   *
   * @param id id of user to delete
   * @throws UserNotFoundException if no user with id exists
   */
  void delete(Long id) throws UserNotFoundException;
}
