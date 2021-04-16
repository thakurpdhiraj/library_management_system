package com.dhitha.lms.order.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for exchange with the Inventory Service
 *
 * @author Dhiraj
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long bookId;

  private String isbn;

  private String bookReferenceId;

  private Integer categoryId;
}
