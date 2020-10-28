package com.dhitha.lms.order.repository;

import com.dhitha.lms.order.entity.BookOrderHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link BookOrderHistory}
 *
 * @author Dhiraj
 */
@Repository
public interface BookOrderHistoryRepository extends JpaRepository<BookOrderHistory, Long> {

  /**
   * Find all order history of a user
   *
   * @param userId -
   * @return List of order histories of a user, empty list if none found
   */
  List<BookOrderHistory> findByUserId(Long userId);
}
