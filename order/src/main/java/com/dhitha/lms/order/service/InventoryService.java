package com.dhitha.lms.order.service;

import com.dhitha.lms.order.dto.InventoryDTO;
import com.dhitha.lms.order.error.GenericException;

/**
 * Service to connect to Inventory service
 *
 * @author Dhiraj
 */
public interface InventoryService {

  /**
   * Connect with inventory service and place an order if available
   *
   * @param bookId -
   * @return inventory details if order was successful
   * @throws GenericException if inventory was not found for book or service was unavailable
   */
  InventoryDTO orderIfAvailable(Long bookId) throws GenericException;

  /**
   * Connect with inventory service and return a book
   *
   * @param bookId -
   * @param bookReferenceId -
   * @throws GenericException if inventory was not found for book or service was unavailable
   */
  void returnBook(Long bookId, String bookReferenceId) throws GenericException;
}
