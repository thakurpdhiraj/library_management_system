package com.dhitha.lms.inventory.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Embeddable Id for {@link Inventory}
 * @author Dhiraj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class InventoryId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "book_id", updatable = false, nullable = false)
    private Long bookId;

    @Column(name = "isbn", updatable = false, nullable = false)
    private String isbn;

    @Column(name = "book_reference_id", updatable = false, nullable = false)
    private String bookReferenceId;
}
