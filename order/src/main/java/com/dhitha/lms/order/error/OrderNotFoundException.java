package com.dhitha.lms.order.error;

/**
 * Exception in case order is not found in LMS
 *
 * @author Dhiraj
 */
public class OrderNotFoundException extends Exception {
  public OrderNotFoundException(String message) {
    super(message);
  }
}
