package com.dhitha.lms.book.service;

import com.dhitha.lms.book.dto.BookDTO;
import com.dhitha.lms.book.error.BookNotFoundException;
import java.util.List;

/**
 * Service for {@link BookDTO}
 *
 * @author Dhiraj
 */
public interface BookService {

  /**
   * Find all books in LMS
   *
   * @param bookDTO books to search
   * @return list of books
   */
  List<BookDTO> findAll(BookDTO bookDTO);

  /**
   * Find book with id
   *
   * @param id -
   * @return book corresponding to the id
   * @throws BookNotFoundException if no book exists with id
   */
  BookDTO findById(Long id) throws BookNotFoundException;

  /**
   * Save a book
   *
   * @param bookDTO book to save
   * @return saved book with new generated id
   */
  BookDTO save(BookDTO bookDTO);

  /**
   * Update a book
   *
   * @param bookDTO book to update, null values will be ignored
   * @return updated book
   * @throws BookNotFoundException if no book exists with id
   */
  BookDTO update(BookDTO bookDTO) throws BookNotFoundException;

  /**
   * Delete a book
   *
   * @param id -
   * @throws BookNotFoundException if no book exists with id
   */
  void delete(Long id) throws BookNotFoundException;
}
