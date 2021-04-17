package com.dhitha.lms.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dhitha.lms.order.dto.BookOrderDTO;
import com.dhitha.lms.order.dto.InventoryDTO;
import com.dhitha.lms.order.entity.BookOrder;
import com.dhitha.lms.order.error.GenericException;
import com.dhitha.lms.order.error.OrderNotFoundException;
import com.dhitha.lms.order.repository.BookOrderRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * Unit tests for {@link BookOrderService}
 *
 * @author Dhiraj
 */
@ExtendWith(MockitoExtension.class)
class BookOrderServiceTest {

  @Mock private BookOrderRepository repositoryMock;
  @Mock private BookOrderHistoryService historyMock;
  @Mock private InventoryService inventoryServiceMock;
  private final ModelMapper modelMapper = new ModelMapper();

  private BookOrderService subject;

  @BeforeEach
  void init() {
    subject =
        new BookOrderServiceImpl(repositoryMock, historyMock, modelMapper, inventoryServiceMock);
  }

  @Test
  @DisplayName("findById: order found, expected success")
  void testFindByIdSuccess() throws Exception {
    when(repositoryMock.findById(1L)).thenReturn(Optional.of(BookOrder.builder().build()));
    subject.findById(1L);
    verify(repositoryMock).findById(1L);
  }

  @Test
  @DisplayName("findById: order not found, expected OrderNotFoundException")
  void testFindByIdNotFound() {
    assertThrows(
        OrderNotFoundException.class,
        () -> {
          when(repositoryMock.findById(1L)).thenReturn(Optional.empty());
          subject.findById(1L);
        });
    verify(repositoryMock).findById(1L);
  }

  @Test
  @DisplayName("findAllByUser: order found, expected success")
  void testFindAllByUserIdSuccess() {
    when(repositoryMock.findByUserId(1L)).thenReturn(Collections.emptyList());
    subject.findAllByUser(1L);
    verify(repositoryMock).findByUserId(1L);
  }

  @Test
  @DisplayName("findAllByBook: order found, expected success")
  void testFindAllByBookSuccess() {
    when(repositoryMock.findByBookId(1L)).thenReturn(Collections.emptyList());
    subject.findAllByBook(1L);
    verify(repositoryMock).findByBookId(1L);
  }

  @Test
  @DisplayName("findAllReturnOverdue: order found, expected success")
  void testFindAllReturnOverdueSuccess() {
    when(repositoryMock.findByReturnByBefore(any())).thenReturn(Collections.emptyList());
    subject.findAllReturnOverdue();
    verify(repositoryMock).findByReturnByBefore(any(LocalDateTime.class));
  }

  @Test
  @DisplayName("findAllReturnOverdueForUser: order found, expected success")
  void testFindAllReturnOverdueForUserSuccess() {
    when(repositoryMock.findByReturnByBeforeAndUserId(any(), eq(1L)))
        .thenReturn(Collections.emptyList());
    subject.findAllReturnOverdueForUser(1L);
    verify(repositoryMock).findByReturnByBeforeAndUserId(any(LocalDateTime.class), eq(1L));
  }

  @Test
  @DisplayName("findAllCollectionOverdue: order found, expected success")
  void testFindAllCollectionOverdueSuccess() {
    when(repositoryMock.findByCollectByBefore(any())).thenReturn(Collections.emptyList());
    subject.findAllCollectionOverdue();
    verify(repositoryMock).findByCollectByBefore(any(LocalDateTime.class));
  }

  @Test
  @DisplayName("findAllCollectionOverdueForUser: order found, expected success")
  void testFindAllCollectionOverdueForUserSuccess() {
    when(repositoryMock.findByCollectByBeforeAndUserId(any(), eq(1L)))
        .thenReturn(Collections.emptyList());
    subject.findAllCollectionOverdueForUser(1L);
    verify(repositoryMock).findByCollectByBeforeAndUserId(any(LocalDateTime.class), eq(1L));
  }

  @Test
  @DisplayName("orderBook: order criteria passed, expected success")
  void testOrderBookSuccess() throws Exception {
    when(repositoryMock.existsByUserIdAndBookId(1L, 1L)).thenReturn(false);
    when(inventoryServiceMock.orderIfAvailable(1L))
        .thenReturn(InventoryDTO.builder().bookId(1L).bookReferenceId("abc").build());
    when(repositoryMock.saveAndFlush(any(BookOrder.class)))
        .thenReturn(BookOrder.builder().bookReferenceId("abc").bookId(1L).userId(1L).build());
    BookOrderDTO result = subject.orderBook(BookOrderDTO.builder().bookId(1L).userId(1L).build());

    assertEquals(1L, result.getBookId());
    assertEquals(1L, result.getUserId());
    assertEquals("abc", result.getBookReferenceId());

    verify(repositoryMock).existsByUserIdAndBookId(1L, 1L);
    verify(inventoryServiceMock).orderIfAvailable(1L);
    verify(repositoryMock).saveAndFlush(any(BookOrder.class));
  }

  @Test
  @DisplayName(
      "orderBook: user already has order with the input book id, expected GenericException")
  void testOrderBookAlreadyExistsForSameUserAndBookCombination() throws Exception {
    assertThrows(
        GenericException.class,
        () -> {
          when(repositoryMock.existsByUserIdAndBookId(1L, 1L)).thenReturn(true);
          subject.orderBook(BookOrderDTO.builder().bookId(1L).userId(1L).build());
        });
    verify(repositoryMock).existsByUserIdAndBookId(1L, 1L);
    verify(inventoryServiceMock, never()).orderIfAvailable(1L);
    verify(repositoryMock, never()).saveAndFlush(any(BookOrder.class));
  }

  @Test
  @DisplayName("collectBook: order collection criteria satisfied, expected success")
  void testCollectBookSuccess() throws Exception {
    BookOrder orderMock = BookOrder.builder().id(1L).build();
    when(repositoryMock.findById(1L)).thenReturn(Optional.of(orderMock));
    when(repositoryMock.saveAndFlush(any(BookOrder.class))).thenReturn(orderMock);
    subject.collectBook(1L);
    verify(repositoryMock).findById(1L);
    verify(repositoryMock).saveAndFlush(any(BookOrder.class));
  }

  @Test
  @DisplayName("collectBook: order not found, expected OrderNotFoundException")
  void testCollectBookOrderNotFound() {

    assertThrows(
        OrderNotFoundException.class,
        () -> {
          when(repositoryMock.findById(1L)).thenReturn(Optional.empty());
          subject.collectBook(1L);
        });
    verify(repositoryMock).findById(1L);
    verify(repositoryMock, never()).saveAndFlush(any(BookOrder.class));
  }

  @Test
  @DisplayName("collectBook: order already collected, expected OrderNotFoundException")
  void testCollectBookOrderAlreadyCollected() {
    assertThrows(
        OrderNotFoundException.class,
        () -> {
          BookOrder orderMock = BookOrder.builder().id(1L).collectedAt(LocalDateTime.now()).build();
          when(repositoryMock.findById(1L)).thenReturn(Optional.of(orderMock));
          subject.collectBook(1L);
        });
    verify(repositoryMock).findById(1L);
    verify(repositoryMock, never()).saveAndFlush(any(BookOrder.class));
  }

  @Test
  @DisplayName("returnBook: order return criteria satisfied, expected success")
  void testReturnBookSuccess() throws Exception {
    LocalDateTime returnBy = LocalDateTime.now().plusDays(2);
    BookOrder orderMock = BookOrder.builder().id(1L).bookId(1L).returnBy(returnBy)
        .bookReferenceId("abc").build();
    when(repositoryMock.findById(1L)).thenReturn(Optional.of(orderMock));
    doNothing().when(inventoryServiceMock).returnBook(1L, "abc");
    doNothing().when(historyMock).save(any(BookOrder.class));
    doNothing().when(repositoryMock).deleteById(1L);
    subject.returnBook(1L);
    verify(repositoryMock).findById(1L);
    verify(inventoryServiceMock).returnBook(1L, "abc");
    verify(historyMock).save(any(BookOrder.class));
    verify(repositoryMock).deleteById(1L);
  }

  @Test
  @DisplayName("returnBook: order return overdue, late fees expected, expected success")
  void testReturnBookSuccessWithLateFees() throws Exception {
    LocalDateTime returnBy = LocalDateTime.now().minusDays(2);
    BookOrder orderMock = BookOrder.builder().id(1L).bookId(1L).returnBy(returnBy)
        .bookReferenceId("abc").build();
    when(repositoryMock.findById(1L)).thenReturn(Optional.of(orderMock));
    doNothing().when(inventoryServiceMock).returnBook(1L, "abc");
    doNothing().when(historyMock).save(any(BookOrder.class));
    doNothing().when(repositoryMock).deleteById(1L);
    BookOrderDTO result = subject.returnBook(1L);
    assertEquals(200, result.getLateFees());
    verify(repositoryMock).findById(1L);
    verify(inventoryServiceMock).returnBook(1L, "abc");
    verify(historyMock).save(any(BookOrder.class));
    verify(repositoryMock).deleteById(1L);
  }

  @Test
  @DisplayName("returnBook: order not found to return, expected OrderNotFoundException")
  void testReturnBookOrderNotFound() throws Exception{

    assertThrows(
        OrderNotFoundException.class,
        () -> {
          when(repositoryMock.findById(1L)).thenReturn(Optional.empty());
          subject.returnBook(1L);
        });
    verify(repositoryMock).findById(1L);
    verify(inventoryServiceMock,never()).returnBook(anyLong(), anyString());
    verify(historyMock, never()).save(any(BookOrder.class));
    verify(repositoryMock, never()).deleteById(1L);
  }

  @Test
  @DisplayName("delete: delete criteria satisfied, expected success")
  void testDelete() throws Exception {
    doNothing().when(repositoryMock).deleteById(1L);
    subject.delete(1L);
    verify(repositoryMock).deleteById(1L);
  }

  @Test
  @DisplayName("delete: order not found, expected OrderNotFoundException")
  void testDeleteOrderNotFound() {
    assertThrows(
        OrderNotFoundException.class,
        () -> {
          doThrow(EmptyResultDataAccessException.class).when(repositoryMock).deleteById(1L);
          subject.delete(1L);
        });
    verify(repositoryMock).deleteById(1L);
  }
}
