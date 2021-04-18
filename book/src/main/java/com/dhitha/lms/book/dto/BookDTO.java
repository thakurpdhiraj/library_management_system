package com.dhitha.lms.book.dto;

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
 * DTO for {@link com.dhitha.lms.book.entity.Book}
 *
 * @author Dhiraj
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long id;

  @NotEmpty @ISBN
  private String isbn;

  @NotEmpty private String name;

  @NotNull private CategoryDTO category;

  @NotEmpty private String author;

  @NotEmpty private String publication;

  @NotNull private Integer pages;

  private Integer publicationYear;

  private String summary;

  @JsonProperty(access = Access.READ_ONLY)
  private LocalDateTime addedAt;
}
