package com.dhitha.lms.user.error;

/**
 * In case user is not found in LMS
 *
 * @author Dhiraj
 */
public class UserNotFoundException extends Exception {
  public UserNotFoundException(String message) {
    super(message);
  }
}
