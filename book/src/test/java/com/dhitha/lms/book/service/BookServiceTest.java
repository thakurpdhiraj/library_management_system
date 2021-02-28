package com.dhitha.lms.book.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dhitha.lms.book.dto.BookDTO;
import com.dhitha.lms.book.entity.Book;
import com.dhitha.lms.book.error.BookNotFoundException;
import com.dhitha.lms.book.repository.BookRepository;
import com.dhitha.lms.book.repository.BookSpecification;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;

/**
 * Unit test for {@link BookService}
 *
 * @author Dhiraj
 */
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

  private final ModelMapper modelMapper = new ModelMapper();
  @Mock private BookRepository bookRepositoryMock;
  private BookService subject;

  @BeforeEach
  void init() {
    subject = new BookServiceImpl(bookRepositoryMock, modelMapper);
  }

  /* ********************** findAll ************************** */
  @Test
  void testFindAll() {
    Book book = Book.builder().name("book").build();
    when(bookRepositoryMock.findAll(any(BookSpecification.class))).thenReturn(Collections.singletonList(book));
    List<BookDTO> result = subject.findAll(BookDTO.builder().name("book").build());
    verify(bookRepositoryMock).findAll(any(BookSpecification.class));
    assertEquals(1, result.size());
    assertEquals("book", result.get(0).getName());
  }

  /* ********************** findById ************************** */
  @Test
  void testFindById() throws Exception {
    Book book = Book.builder().name("book").build();
    when(bookRepositoryMock.findById(1L)).thenReturn(Optional.of(book));
    BookDTO result = subject.findById(1L);
    verify(bookRepositoryMock).findById(1L);
    assertEquals(book.getName(), result.getName());
  }

  @Test
  void testFindByIdThrowsBookNotFoundException() throws Exception {
    assertThrows(
        BookNotFoundException.class,
        () -> {
          when(bookRepositoryMock.findById(1L)).thenReturn(Optional.empty());
          subject.findById(1L);
        });
    verify(bookRepositoryMock).findById(1L);
  }

  /* ********************** save ************************** */
  @Test
  void testSave() {
    BookDTO bookDTO = BookDTO.builder().name("book").author("author").build();
    Book book = Book.builder().id(1L).name("book").author("author").build();
    when(bookRepositoryMock.saveAndFlush(any(Book.class))).thenReturn(book);
    BookDTO result = subject.save(bookDTO);
    verify(bookRepositoryMock).saveAndFlush(any(Book.class));
    assertEquals(1L, result.getId());
  }
  /* ********************** update ************************** */
  @Test
  void testUpdate() throws Exception {
    BookDTO bookDTO = BookDTO.builder().id(1L).name("new").build();
    Book book = Book.builder().id(1L).name("book").author("author").build();
    when(bookRepositoryMock.findById(1L)).thenReturn(Optional.of(book));
    book.setName("new");
    when(bookRepositoryMock.saveAndFlush(any(Book.class))).thenReturn(book);
    BookDTO result = subject.update(bookDTO);
    verify(bookRepositoryMock).saveAndFlush(any(Book.class));
    assertEquals(1L, result.getId());
    assertEquals("new", result.getName());
  }

  @Test
  void testUpdateThrowsBookNotFoundException() {
    assertThrows(
        BookNotFoundException.class,
        () -> {
          when(bookRepositoryMock.findById(1L)).thenReturn(Optional.empty());
          subject.update(BookDTO.builder().id(1L).build());
        });
    verify(bookRepositoryMock).findById(1L);
    verify(bookRepositoryMock, never()).saveAndFlush(any(Book.class));
  }
  /* ********************** delete ************************** */

  @Test
  void testDelete() throws Exception {
    doNothing().when(bookRepositoryMock).deleteById(1L);
    subject.delete(1L);
    verify(bookRepositoryMock).deleteById(1L);
  }

  @Test
  void testDeleteThrowsBookNotFoundException() {
    assertThrows(
        BookNotFoundException.class,
        () -> {
          doThrow(EmptyResultDataAccessException.class).when(bookRepositoryMock).deleteById(20L);
          subject.delete(20L);
        });
    verify(bookRepositoryMock).deleteById(20L);
  }
}
