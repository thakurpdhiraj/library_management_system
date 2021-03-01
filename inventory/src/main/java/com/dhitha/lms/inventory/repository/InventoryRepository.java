package com.dhitha.lms.inventory.repository;

import com.dhitha.lms.inventory.entity.Inventory;
import com.dhitha.lms.inventory.entity.InventoryId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Inventory}
 *
 * @author Dhiraj
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, InventoryId> {

  /**
   * Find all inventory details by book id
   *
   * @param bookId -
   * @return List of inventory for existing book id
   */
  List<Inventory> findByIdBookId(Long bookId);

  /**
   * Find book with given book and reference id
   *
   * @param bookId -
   * @param bookReferenceId -
   * @return optional containing inventory or empty optional
   */
  Optional<Inventory> findByIdBookIdAndIdBookReferenceId(Long bookId, String bookReferenceId);

  /**
   * Number of inventory available for book id
   *
   * @param bookId -
   * @return count of books
   */
  long countByIdBookIdAndAvailable(Long bookId, boolean available);

  /**
   * Delete all inventory corresponding to book id
   *
   * @param bookId -
   */
  void deleteByIdBookId(Long bookId);

  /**
   * Delete all inventory corresponding to book id and book reference id
   *
   * @param bookId -
   * @param bookReferenceId -
   */
  void deleteByIdBookIdAndIdBookReferenceId(Long bookId, String bookReferenceId);

  /**
   * Update the availability of inventory
   *
   * @param bookId -
   * @param bookReferenceId -
   * @param available true - make it available / false - make it not available
   * @return number of row affected
   */
  @Modifying
  @Query(
      "update Inventory iv set iv.available=:available where iv.id.bookId=:bookId and iv.id.bookReferenceId=:bookReferenceId")
  int updateAvailability(
      @Param("bookId") Long bookId,
      @Param("bookReferenceId") String bookReferenceId,
      @Param("available") boolean available);
}
