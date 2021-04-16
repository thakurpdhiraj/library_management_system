package com.dhitha.lms.inventory.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity to represent inventory in LMS
 *
 * @author Dhiraj
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
    name = "inventory",
    uniqueConstraints =
        @UniqueConstraint(
            name = "unique_id_reference",
            columnNames = {"book_id", "isbn", "book_reference_id"}))
public class Inventory implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId private InventoryId id;

  @Column(name = "category_id", updatable = false)
  private Integer categoryId;

  @Column(name = "available", columnDefinition = "BIT(1) DEFAULT 1")
  private Boolean available;
}
