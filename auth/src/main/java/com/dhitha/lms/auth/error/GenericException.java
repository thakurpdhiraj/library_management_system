package com.dhitha.lms.auth.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Generic Exception for Auth Service
 *
 * @author Dhiraj
 */
public class GenericException extends Exception {

  @Getter
  private final String message;
  @Getter
  private final int status;

  public GenericException(){
    super("Something went wrong");
    this.message = "Something went wrong";
    this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
  }

  public GenericException(String message, int status) {
    super(message);
    this.message = message;
    this.status = status;
  }
}