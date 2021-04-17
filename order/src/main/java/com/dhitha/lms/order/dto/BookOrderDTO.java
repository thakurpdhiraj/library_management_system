package com.dhitha.lms.order.dto;

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
import org.hibernate.validator.constraints.ISBN;

/**
 * DTO for {@link com.dhitha.lms.order.entity.BookOrder}
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

  @NotNull private Long userId;

  @NotNull private Long bookId;

  @NotEmpty @ISBN
  private String bookIsbn;

  @NotEmpty private String bookName;

  @JsonProperty(access = Access.READ_ONLY)
  private String bookReferenceId;

  @JsonProperty(access = Access.READ_ONLY)
  private LocalDateTime orderedAt;

  @JsonProperty(access = Access.READ_ONLY)
  private LocalDateTime collectedAt;

  @JsonProperty(access = Access.READ_ONLY)
  private LocalDateTime collectBy;

  @JsonProperty(access = Access.READ_ONLY)
  private LocalDateTime returnedAt;

  @JsonProperty(access = Access.READ_ONLY)
  private LocalDateTime returnBy;

  @JsonProperty(access = Access.READ_ONLY)
  private Long lateFees;
}
