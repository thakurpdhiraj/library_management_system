package com.dhitha.lms.order.service;

import com.dhitha.lms.order.dto.BookOrderDTO;
import com.dhitha.lms.order.entity.BookOrder;
import com.dhitha.lms.order.error.GenericException;
import com.dhitha.lms.order.error.OrderNotFoundException;
import java.util.List;

/**
 * Service for {@link BookOrder}
 *
 * @author Dhiraj
 */
public interface BookOrderService {

  /**
   * Find a book order by id
   *
   * @param id - order id
   * @return order details
   * @throws OrderNotFoundException if no order present with corresponding id
   */
  BookOrderDTO findById(Long id) throws OrderNotFoundException;

  /**
   * Find all orders of a user
   *
   * @param userId -
   * @return list of orders or empty list if none found
   */
  List<BookOrderDTO> findAllByUser(Long userId);

  /**
   * Find all orders for a book
   *
   * @param bookId -
   * @return list of orders or empty list if none found
   */
  List<BookOrderDTO> findAllByBook(Long bookId);

  /**
   * Find all orders for which the returned date is overdue
   *
   * @return list of
   */
  List<BookOrderDTO> findAllReturnOverdue();

  /**
   * Find all orders for which the returned date is overdue for a user
   *
   * @param userId
   * @return
   */
  List<BookOrderDTO> findAllReturnOverdueForUser(Long userId);

  /**
   * Find all order for which collection date is overdue
   *
   * @return
   */
  List<BookOrderDTO> findAllCollectionOverdue();

  /**
   * Find all orders for which collection date is overdue for a user
   *
   * @param userId
   * @return
   */
  List<BookOrderDTO> findAllCollectionOverdueForUser(Long userId);

  /**
   * Order a new book, Connects with Inventory service to fnd if book is available
   *
   * @param orderDTO order details to add if possible
   * @return new order with generated id
   * @throws GenericException if order cannot be placed due to book/service unavailability
   */
  BookOrderDTO orderBook(BookOrderDTO orderDTO) throws GenericException;

  /**
   * Mark a order as collected
   *
   * @param id - order id
   * @return order detail with collectedAt date marked as present datetime
   * @throws OrderNotFoundException if no order present with id
   */
  BookOrderDTO collectBook(Long id) throws OrderNotFoundException;

  /**
   * Mark the book as returned, Connects with inventory servie to return the book and deletes the
   * order and adds it to order history
   *
   * @param id order id
   * @throws OrderNotFoundException if no order present with id
   * @throws GenericException if order cannot be returned due to book/service unavailability
   */
  BookOrderDTO returnBook(Long id) throws OrderNotFoundException, GenericException;

  /**
   * Delete an order
   *
   * @param id
   * @throws OrderNotFoundException
   */
  void delete(Long id) throws OrderNotFoundException;
}
