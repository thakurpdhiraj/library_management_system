package com.dhitha.lms.inventory.error;

/**
 * Exception in case no inventory available in LMS for provided input
 * @author Dhiraj
 */
public class InventoryNotFoundException extends Exception{

  public InventoryNotFoundException(String message) {
    super(message);
  }
}
