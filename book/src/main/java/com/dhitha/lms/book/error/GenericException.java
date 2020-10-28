package com.dhitha.lms.book.error;

/**
 * Generic Exception for book
 *
 * @author Dhiraj
 */
public class GenericException extends Exception {

  private final String message;
  private final int status;

  public GenericException(String message, int status) {
    super(message);
    this.message = message;
    this.status = status;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public int getStatus() {
    return status;
  }
}
