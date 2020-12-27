package com.dhitha.lms.book.dto;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.dhitha.lms.book.entity.Category}
 *
 * @author Dhiraj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private Integer id;

  @NotEmpty
  private String name;
}
