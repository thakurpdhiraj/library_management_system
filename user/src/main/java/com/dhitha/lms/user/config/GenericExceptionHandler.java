package com.dhitha.lms.user.config;

import com.dhitha.lms.user.dto.ErrorDTO;
import com.dhitha.lms.user.error.GenericException;
import com.dhitha.lms.user.error.UserNotFoundException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Common Exception Handler
 *
 * @author Dhiraj
 */
@ControllerAdvice
@Log4j2
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {

  @NonNull
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatus status,
      @NonNull WebRequest request) {
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

  @NonNull
  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex,
      Object body,
      @NonNull HttpHeaders headers,
      HttpStatus status,
      @NonNull WebRequest request) {
    log.error("handleExceptionInternal():{} -> {}", ex.getCause(), ex);
    ErrorDTO err =
        ErrorDTO.builder()
            .error("invalid_request")
            .error_description(ex.getLocalizedMessage())
            .status(status.value())
            .timestamp(LocalDateTime.now())
            .build();
    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler({UserNotFoundException.class})
  private ResponseEntity<ErrorDTO> handleUserNotFound(Exception ex) {
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
        "handleNotFound():{} -> {} : status: {}", ex.getCause(), ex.getMessage(), ex.getStatus());
    ErrorDTO err =
        ErrorDTO.builder()
            .error("invalid_request")
            .error_description(ex.getLocalizedMessage())
            .status(ex.getStatus())
            .timestamp(LocalDateTime.now())
            .build();
    return ResponseEntity.status(ex.getStatus()).body(err);
  }

  @ExceptionHandler({IllegalArgumentException.class})
  private ResponseEntity<ErrorDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
    ErrorDTO err =
        ErrorDTO.builder()
            .error("invalid_request")
            .error_description(ex.getLocalizedMessage())
            .status(HttpStatus.BAD_REQUEST.value())
            .timestamp(LocalDateTime.now())
            .build();
    return ResponseEntity.badRequest().body(err);
  }

  @ExceptionHandler({Exception.class})
  private ResponseEntity<ErrorDTO> handleException(Exception ex) {
    log.error("handleException():{} -> {}", ex.getCause(), ex);
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
