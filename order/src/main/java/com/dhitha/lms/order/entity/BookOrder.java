package com.dhitha.lms.order.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Entity for all orders in LMS
 *
 * @author Dhiraj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "book_order",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "unique_user_book",
          columnNames = {"user_id", "book_id"})
    })
public class BookOrder implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false, updatable = false)
  private Long userId;

  @Column(name = "book_id", nullable = false, updatable = false)
  private Long bookId;

  @Column(name = "book_isbn", nullable = false, updatable = false)
  private String bookIsbn;

  @Column(name = "book_name", nullable = false, updatable = false)
  private String bookName;

  @Column(name = "book_reference_id", unique = true, nullable = false, updatable = false)
  private String bookReferenceId;

  @CreationTimestamp
  @Column(name = "ordered_at", columnDefinition = "TIMESTAMP", updatable = false)
  private LocalDateTime orderedAt;

  @Column(name = "collected_at", columnDefinition = "TIMESTAMP")
  private LocalDateTime collectedAt;

  @Column(name = "collect_by", columnDefinition = "TIMESTAMP", updatable = false)
  private LocalDateTime collectBy;

  @Column(name = "returned_at", columnDefinition = "TIMESTAMP")
  private LocalDateTime returnedAt;

  @Column(name = "return_by", columnDefinition = "TIMESTAMP", updatable = false)
  private LocalDateTime returnBy;

  @Column(name = "late_fees", updatable = false)
  private Long lateFees;
}
