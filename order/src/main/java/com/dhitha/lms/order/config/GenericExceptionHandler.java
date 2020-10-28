package com.dhitha.lms.order.config;


import com.dhitha.lms.order.dto.ErrorDTO;
import com.dhitha.lms.order.error.GenericException;
import com.dhitha.lms.order.error.OrderNotFoundException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Common Exception Handler
 *
 * @author Dhiraj
 */
@ControllerAdvice
@Log4j2
public class GenericExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  private ResponseEntity<ErrorDTO> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex) {
    log.error("handleMethodArgumentNotValid():{} -> {}", ex.getCause(), ex.getBindingResult());
    BindingResult bindingResult = ex.getBindingResult();
    String description =
        bindingResult.getFieldErrors().stream()
            .map(
                objectError ->
                    String.join(" : ", objectError.getField(), objectError.getDefaultMessage()))
            .collect(Collectors.joining(", "));
    ErrorDTO err =
        ErrorDTO.builder()
            .error("invalid_input")
            .error_description(description)
            .status(HttpStatus.BAD_REQUEST.value())
            .timestamp(LocalDateTime.now())
            .build();
    return ResponseEntity.badRequest().body(err);
  }

  @ExceptionHandler({
    HttpRequestMethodNotSupportedException.class,
    MissingServletRequestParameterException.class,
    MissingPathVariableException.class,
    IllegalArgumentException.class
  })
  private ResponseEntity<ErrorDTO> handleInvalidRequest(Exception ex) {
    log.error("handleInvalidRequest():{} -> {}", ex.getCause(), ex.getMessage());
    ErrorDTO err =
        ErrorDTO.builder()
            .error("invalid_request")
            .error_description(ex.getLocalizedMessage())
            .status(HttpStatus.BAD_REQUEST.value())
            .timestamp(LocalDateTime.now())
            .build();
    return ResponseEntity.badRequest().body(err);
  }

  @ExceptionHandler({
    OrderNotFoundException.class,
    NoHandlerFoundException.class
  })
  private ResponseEntity<ErrorDTO> handleNotFound(Exception ex) {
    log.error("handleNotFound():{} -> {}", ex.getCause(), ex.getMessage());
    ErrorDTO err =
        ErrorDTO.builder()
            .error("invalid_request")
            .error_description(ex.getLocalizedMessage())
            .status(HttpStatus.NOT_FOUND.value())
            .timestamp(LocalDateTime.now())
            .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
  }

  @ExceptionHandler({GenericException.class})
  private ResponseEntity<ErrorDTO> handleGeneric(GenericException ex) {
    log.error(
        "handleGeneric():{} -> {} : status: {}", ex.getCause(), ex.getMessage(), ex.getStatus());
    ErrorDTO err =
        ErrorDTO.builder()
            .error("invalid_request")
            .error_description(ex.getLocalizedMessage())
            .status(ex.getStatus())
            .timestamp(LocalDateTime.now())
            .build();
    return ResponseEntity.status(ex.getStatus()).body(err);
  }

  @ExceptionHandler({Exception.class})
  private ResponseEntity<ErrorDTO> handleException(Exception ex) {
    log.error("handleException():",ex);
    ErrorDTO err =
        ErrorDTO.builder()
            .error("server_error")
            .error_description("Something went wrong")
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .timestamp(LocalDateTime.now())
            .build();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
  }
}
