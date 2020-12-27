package com.dhitha.lms.order.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dhitha.lms.order.entity.BookOrder;
import com.dhitha.lms.order.entity.BookOrderHistory;
import com.dhitha.lms.order.repository.BookOrderHistoryRepository;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

/**
 * Unit Tests for {@link BookOrderHistoryService }
 *
 * @author Dhiraj
 */
@ExtendWith(MockitoExtension.class)
class BookOrderHistoryServiceTest {

  @Mock private BookOrderHistoryRepository repositoryMock;
  private BookOrderHistoryService subject;

  @BeforeEach
  void init() {
    subject = new BookOrderHistoryServiceImpl(repositoryMock, new ModelMapper());
  }

  @Test
  void testSave() {
    when(repositoryMock.saveAndFlush(
            BookOrderHistory.builder().orderId(1L).bookId(1L).bookReferenceId("abc").build()))
        .thenReturn(
            BookOrderHistory.builder().orderId(1L).bookId(1L).bookReferenceId("abc").build());
    subject.save(BookOrder.builder().id(1L).bookId(1L).bookReferenceId("abc").build());
    verify(repositoryMock).saveAndFlush(any(BookOrderHistory.class));
  }

  @Test
  void testFindAllOfUser() {
    when(repositoryMock.findByUserId(1L)).thenReturn(Collections.singletonList(BookOrderHistory.builder().orderId(1L).bookId(1L).bookReferenceId("abc").build()));
    subject.findAllForUser(1L);
    verify(repositoryMock).findByUserId(1L);
  }
}
