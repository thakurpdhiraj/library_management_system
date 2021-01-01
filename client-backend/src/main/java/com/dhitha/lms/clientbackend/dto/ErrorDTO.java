package com.dhitha.lms.clientbackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * Common DTO for all Errors
 *
 * @author Dhiraj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private String error;

  private String error_description;

  private int status;

  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDateTime timestamp;
}
