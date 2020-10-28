package com.dhitha.lms.order.repository;

import com.dhitha.lms.order.entity.BookOrder;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link BookOrder}
 *
 * @author Dhiraj
 */
@Repository
public interface BookOrderRepository extends JpaRepository<BookOrder, Long> {

  /**
   * Find all orders for a book id
   *
   * @param bookId -
   * @return list of orders for a book id or empty list if none found
   */
  List<BookOrder> findByBookId(Long bookId);

  /**
   * Find all orders for a user id
   *
   * @param userId
   * @return list of orders for a user id or empty list if none found
   */
  List<BookOrder> findByUserId(Long userId);

  /**
   * Find all the orders of books that have returnBy date before the passed date
   *
   * @param currentDate -
   * @return list of orders or empty list if none found
   */
  List<BookOrder> findByReturnByBefore(LocalDateTime currentDate);

  /**
   * Find all the orders of books that have collectBy date before the passed date
   *
   * @param currentDate -
   * @return list of orders or empty list if none found
   */
  List<BookOrder> findByCollectByBefore(LocalDateTime currentDate);

  /**
   * Find all the orders of books that have returnBy date before the passed date for a user
   *
   * @param currentDate -
   * @param userId -
   * @return list of orders or empty list if none found
   */
  List<BookOrder> findByReturnByBeforeAndUserId(LocalDateTime currentDate, Long userId);

  /**
   * Find all the orders of books that have collectBy date before the passed date for a user
   *
   * @param currentDate -
   * @param userId -
   * @return list of orders or empty list if none found
   */
  List<BookOrder> findByCollectByBeforeAndUserId(LocalDateTime currentDate, Long userId);

  /**
   * Check if order exists for a user for a book
   *
   * @param userId -
   * @param bookId -
   * @return true if exists otherwise false
   */
  boolean existsByUserIdAndBookId(Long userId, Long bookId);
}
