package com.dhitha.lms.order.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Immutable;

/**
 * Keeps Record for History of Order
 *
 * @author Dhiraj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Immutable
@Table(name = "book_order_history")
public class BookOrderHistory implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "order_id", nullable = false, unique = true)
  private Long orderId;

  @Column(name = "user_id", nullable = false, updatable = false)
  private Long userId;

  @Column(name = "book_id", nullable = false, updatable = false)
  private Long bookId;

  @Column(name = "book_isbn", nullable = false, updatable = false)
  private String bookIsbn;

  @Column(name = "book_name", nullable = false, updatable = false)
  private String bookName;

  @Column(name = "book_reference_id", nullable = false, updatable = false)
  private String bookReferenceId;

  @Column(name = "ordered_at", columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
  private LocalDateTime orderedAt;

  @Column(
      name = "collected_at",
      columnDefinition = "TIMESTAMP",
      updatable = false)
  private LocalDateTime collectedAt;

  @Column(name = "returned_at", columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
  private LocalDateTime returnedAt;

  @Column(name = "late_fees", updatable = false)
  private Long lateFees;
}
