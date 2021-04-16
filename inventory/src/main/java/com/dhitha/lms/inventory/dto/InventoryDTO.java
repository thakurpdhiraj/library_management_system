package com.dhitha.lms.inventory.dto;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.ISBN;

/**
 * DTO for {@link com.dhitha.lms.inventory.entity.Inventory}
 *
 * @author Dhiraj
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  @NotNull
  private Long bookId;

  @NotEmpty @ISBN
  private String isbn;

  private String bookReferenceId;

  private Integer categoryId;

  private boolean available;
}
