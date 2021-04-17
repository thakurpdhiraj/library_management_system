package com.dhitha.lms.clientbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for orders
 *
 * @author Dhiraj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookOrderDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long id;

  private Long userId;

  @NotNull private Long bookId;

  @NotEmpty private String bookIsbn;

  @NotEmpty private String bookName;

  private String bookReferenceId;

  private LocalDateTime orderedAt;

  private LocalDateTime collectedAt;

  private LocalDateTime collectBy;

  private LocalDateTime returnedAt;

  private LocalDateTime returnBy;

  private Long lateFees;
}
