package com.dhitha.lms.order.service;

import com.dhitha.lms.order.dto.BookOrderHistoryDTO;
import com.dhitha.lms.order.entity.BookOrder;
import java.util.List;

/**
 * Service for {@link com.dhitha.lms.order.entity.BookOrderHistory}
 *
 * @author Dhiraj
 */
public interface BookOrderHistoryService {

  /**
   * Save a history of order
   * @param bookOrder - order to save
   */
  void save(BookOrder bookOrder);

  /**
   * Return history of order for a user
   * @param userId -
   * @return list of orders or empty list if none found
   */
  List<BookOrderHistoryDTO> findAllForUser(Long userId);
}
