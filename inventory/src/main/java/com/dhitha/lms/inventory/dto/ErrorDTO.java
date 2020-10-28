package com.dhitha.lms.inventory.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Common DTO for all error
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

  private LocalDateTime timestamp;
}
