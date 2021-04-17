package com.dhitha.lms.clientbackend.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for book history for user
 * @author Dhiraj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookOrderHistoryDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  private Long orderId;

  private Long userId;

  private Long bookId;

  private String bookIsbn;

  private String bookName;

  private String bookReferenceId;

  private LocalDateTime orderedAt;

  private LocalDateTime collectedAt;

  private LocalDateTime returnedAt;

  private Long lateFees;
}
