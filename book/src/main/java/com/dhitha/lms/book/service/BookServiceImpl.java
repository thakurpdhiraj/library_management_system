package com.dhitha.lms.book.service;

import com.dhitha.lms.book.dto.BookDTO;
import com.dhitha.lms.book.entity.Book;
import com.dhitha.lms.book.error.BookNotFoundException;
import com.dhitha.lms.book.repository.BookRepository;
import com.dhitha.lms.book.repository.BookSpecification;
import java.beans.FeatureDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link BookService}
 *
 * @author Dhiraj
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;

  private final ModelMapper modelMapper;

  @Override
  public List<BookDTO> findAll(BookDTO bookDTO) {
    BookSpecification bookSpecification = new BookSpecification(bookDTO);
    return bookRepository.findAll(bookSpecification).stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public BookDTO findById(Long id) throws BookNotFoundException {
    return bookRepository
        .findById(id)
        .map(this::mapToDTO)
        .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
  }

  @Override
  public BookDTO save(BookDTO bookDTO) {
    bookDTO.setId(null);
    Book book = bookRepository.saveAndFlush(mapToEntity(bookDTO));
    return mapToDTO(book);
  }

  @Override
  public BookDTO update(BookDTO bookDTO) throws BookNotFoundException {
    BookDTO dbBook = this.findById(bookDTO.getId());

    BeanWrapper bw = new BeanWrapperImpl(bookDTO);
    BeanUtils.copyProperties(
        bookDTO,
        dbBook,
        Arrays.stream(bw.getPropertyDescriptors())
            .filter(pd -> bw.getPropertyValue(pd.getName()) == null)
            .map(FeatureDescriptor::getName)
            .toArray(String[]::new));
    Book updatedBook = bookRepository.saveAndFlush(mapToEntity(dbBook));
    return mapToDTO(updatedBook);
  }

  @Override
  public void delete(Long id) throws BookNotFoundException {
    try {
      bookRepository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new BookNotFoundException("Book Not found with id: " + id);
    }
  }

  private BookDTO mapToDTO(Book book) {
    return modelMapper.map(book, BookDTO.class);
  }

  private Book mapToEntity(BookDTO bookDTO) {
    return modelMapper.map(bookDTO, Book.class);
  }
}
