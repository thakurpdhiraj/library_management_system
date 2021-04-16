package com.dhitha.lms.inventory.service;

import com.dhitha.lms.inventory.dto.InventoryDTO;
import com.dhitha.lms.inventory.error.GenericException;
import com.dhitha.lms.inventory.error.InventoryNotFoundException;
import java.util.List;

/**
 * Service for {@link InventoryDTO}
 *
 * @author Dhiraj
 */
public interface InventoryService {

  /**
   * Find all inventory details by book id
   *
   * @param bookId -
   * @return List of inventory for existing book id
   */
  List<InventoryDTO> findAllBook(Long bookId);

  /**
   * Check if any book reference for a book id is available in LMS If yes mark the first book as
   * unavailable and return the marked book
   *
   * @param bookId -
   * @return first available book
   * @throws InventoryNotFoundException if none of the books are available for a book id
   */
  InventoryDTO orderBookIfAvailable(Long bookId) throws InventoryNotFoundException;

  /**
   * Mark the book with corresponding reference as available in inventory
   *
   * @param bookId -
   * @param bookReferenceId -
   * @return true if book is present in LMS and marking the book as available was successful
   */
  boolean returnBook(Long bookId, String bookReferenceId);

  /**
   * Count of books available
   *
   * @param bookId -
   * @return total number of book available for requested book id
   */
  long getAvailableCount(Long bookId);

  /**
   * Add {@literal 'count'} number of books. Generates unique reference number for each book
   *
   * @param inventory DTO containing the book id , isbn , category for the new book
   * @param count total inventory to add
   * @throws GenericException if a book with combination of book id and book reference is already
   *     present
   */
  List<InventoryDTO> add(InventoryDTO inventory, Integer count) throws GenericException;

  /**
   * Delete an inventory of all books by book id
   *
   * @param bookId -
   * @throws InventoryNotFoundException if no inventory present with book id
   */
  void delete(Long bookId) throws InventoryNotFoundException;

  /**
   * Delete specific book reference of a book type
   *
   * @param bookId -
   * @param bookReferenceId -
   * @throws InventoryNotFoundException in case no book is present wih combination of book id and
   *     book reference id
   */
  void delete(Long bookId, List<String> bookReferenceId) throws InventoryNotFoundException;
}
