package com.dhitha.lms.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.dhitha.lms.order.entity.BookOrderHistory}
 *
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
