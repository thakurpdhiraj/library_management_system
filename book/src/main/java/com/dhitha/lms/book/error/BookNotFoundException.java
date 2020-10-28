package com.dhitha.lms.book.error;

/**
 * Exception in case book is not found in LMS
 *
 * @author Dhiraj
 */
public class BookNotFoundException extends Exception {
  public BookNotFoundException(String message) {
    super(message);
  }
}
