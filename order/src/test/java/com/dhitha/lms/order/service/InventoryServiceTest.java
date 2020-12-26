package com.dhitha.lms.order.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dhitha.lms.order.client.InventoryClient;
import com.dhitha.lms.order.dto.InventoryDTO;
import com.dhitha.lms.order.error.GenericException;
import feign.FeignException;
import feign.FeignException.FeignClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link InventoryService}
 *
 * @author Dhiraj
 */
@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

  @Mock private InventoryClient clientMock;

  private InventoryService subject;

  @BeforeEach
  void init() {
    subject = new InventoryServiceImpl(clientMock);
  }

  @Test
  @DisplayName("orderIfAvailable: successful order, expected success")
  void testOrderIfAvailable() throws Exception {
    when(clientMock.orderIfAvailable(1L)).thenReturn(InventoryDTO.builder().build());
    subject.orderIfAvailable(1L);
    verify(clientMock).orderIfAvailable(1L);
  }

  @Test
  @DisplayName("orderIfAvailable: inventory not available to order, expected GenericException")
  void testOrderIfAvailableNotAvailable() throws Exception {
    assertThrows(
        GenericException.class,
        () -> {
          when(clientMock.orderIfAvailable(1L)).thenThrow(FeignException.NotFound.class);
          subject.orderIfAvailable(1L);
          verify(clientMock).orderIfAvailable(1L);
        });
  }

  @Test
  @DisplayName("orderIfAvailable: unknown error on order, expected GenericException")
  void testOrderIfAvailableUnknownError() throws Exception {
    assertThrows(
        GenericException.class,
        () -> {
          when(clientMock.orderIfAvailable(1L)).thenThrow(FeignClientException.class);
          subject.orderIfAvailable(1L);
          verify(clientMock).orderIfAvailable(1L);
        });
  }

  @Test
  @DisplayName("returnBook: inventory client returned true on return of book,expected")
  void testReturnBook() throws Exception {
    when(clientMock.returnBook(1L, "abc")).thenReturn(true);
    subject.returnBook(1L, "abc");
    verify(clientMock).returnBook(anyLong(), anyString());
  }

  @Test
  @DisplayName(
      "returnBook: inventory client returned false on return or book,expected GenericException")
  void testReturnBookUnsuccessful() {
    assertThrows(
        GenericException.class,
        () -> {
          when(clientMock.returnBook(1L, "abc")).thenReturn(false);
          subject.returnBook(1L, "abc");
          verify(clientMock).returnBook(anyLong(), anyString());
        });
  }

  @Test
  @DisplayName("returnBook: ,expected GenericException")
  void testReturnBookUnknownError() {
    assertThrows(
        GenericException.class,
        () -> {
          when(clientMock.returnBook(1L, "abc")).thenThrow(FeignClientException.class);
          subject.returnBook(1L, "abc");
          verify(clientMock).returnBook(anyLong(), anyString());
        });
  }
}
