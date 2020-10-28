package com.dhitha.lms.book.error;

/**
 * Exception in case category is not found in LMS
 *
 * @author Dhiraj
 */
public class CategoryNotFoundException extends Exception {
  public CategoryNotFoundException(String message) {
    super(message);
  }
}
