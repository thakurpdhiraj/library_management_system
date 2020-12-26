package com.dhitha.lms.inventory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dhitha.lms.inventory.dto.InventoryDTO;
import com.dhitha.lms.inventory.entity.Inventory;
import com.dhitha.lms.inventory.entity.InventoryId;
import com.dhitha.lms.inventory.error.GenericException;
import com.dhitha.lms.inventory.error.InventoryNotFoundException;
import com.dhitha.lms.inventory.repository.InventoryRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * Unit tests for {@link InventoryService}
 *
 * @author Dhiraj
 */
@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

  private ModelMapper modelMapper = new ModelMapper();
  @Mock private InventoryRepository repositoryMock;
  private InventoryService subject;

  @BeforeEach
  void init() {
    subject = new InventoryServiceImpl(repositoryMock, modelMapper);
  }

  @Test
  @DisplayName("findAllBook: valid input, expected success")
  void testFindAllOfBook() {
    when(repositoryMock.findByIdBookId(1L)).thenReturn(createMockInventory(1L, true));
    List<InventoryDTO> result = subject.findAllBook(1L);
    assertEquals(2, result.size());
    verify(repositoryMock).findByIdBookId(1L);
  }

  @Test
  @DisplayName("orderBookIfAvailable: book available, expected success")
  void testOrderBookIfAvailable() throws Exception {
    when(repositoryMock.findByIdBookId(1L)).thenReturn(createMockInventory(1L, true));
    when(repositoryMock.updateAvailability(1L, "abc", false)).thenReturn(1);
    InventoryDTO result = subject.orderBookIfAvailable(1L);
    assertEquals("abc", result.getBookReferenceId());
    verify(repositoryMock).findByIdBookId(1L);
    verify(repositoryMock).updateAvailability(any(), any(), eq(false));
  }

  @Test
  @DisplayName(
      "orderBookIfAvailable: book reference not available, expected InventoryNotFoundException")
  void testOrderBookIfAvailableNoBookReference() {
    assertThrows(
        InventoryNotFoundException.class,
        () -> {
          when(repositoryMock.findByIdBookId(1L)).thenReturn(createMockInventory(1L, false));
          subject.orderBookIfAvailable(1L);
          verify(repositoryMock).findByIdBookId(1L);
          verify(repositoryMock, never()).updateAvailability(any(), any(), eq(false));
        });
  }

  @Test
  @DisplayName(
      "orderBookIfAvailable: no book with input id available, expected InventoryNotFoundException")
  void testOrderBookIfAvailableNoBook() {
    assertThrows(
        InventoryNotFoundException.class,
        () -> {
          when(repositoryMock.findByIdBookId(1L)).thenReturn(Collections.emptyList());
          subject.orderBookIfAvailable(1L);
          verify(repositoryMock).findByIdBookId(1L);
          verify(repositoryMock, never()).updateAvailability(anyLong(), anyString(), eq(false));
        });
  }

  @Test
  @DisplayName("returnBook: updating available for book and reference id successful, expected true")
  void testReturnBook() {
    when(repositoryMock.updateAvailability(1L, "abc", true)).thenReturn(1);
    boolean result = subject.returnBook(1L, "abc");
    assertTrue(result);
    verify(repositoryMock).updateAvailability(anyLong(), anyString(), eq(true));
  }

  @Test
  @DisplayName("returnBook: updating available for book and reference id failed, expected false")
  void testReturnBookFail() {
    when(repositoryMock.updateAvailability(1L, "abc", true)).thenReturn(0);
    boolean result = subject.returnBook(1L, "abc");
    assertFalse(result);
    verify(repositoryMock).updateAvailability(anyLong(), anyString(), eq(true));
  }

  @Test
  @DisplayName("getAvailableCount: count for books with available = true, expected success")
  void testGetAvailableCount() {
    when(repositoryMock.countByIdBookIdAndAvailable(1L, true)).thenReturn(1L);
    long result = subject.getAvailableCount(1L);
    assertEquals(1L, result);
    verify(repositoryMock).countByIdBookIdAndAvailable(anyLong(), eq(true));
  }

  @Test
  @DisplayName("add: new inventory added, expected success")
  void testAdd() throws Exception {
    Inventory mockInventory = createMockInventory(1L, true).get(0);
    when(repositoryMock.saveAndFlush(any(Inventory.class))).thenReturn(mockInventory);
    subject.add(InventoryDTO.builder().bookId(1L).bookReferenceId("abc").build());
    verify(repositoryMock).saveAndFlush(any(Inventory.class));
  }

  @Test
  @DisplayName("add: same inventory already present, expected GenericException")
  void testAddWithDuplicate() {
    assertThrows(
        GenericException.class,
        () -> {
          when(repositoryMock.saveAndFlush(any(Inventory.class)))
              .thenThrow(DataIntegrityViolationException.class);
          subject.add(InventoryDTO.builder().bookId(1L).bookReferenceId("abc").build());
          verify(repositoryMock).saveAndFlush(any(Inventory.class));
        });
  }

  @Test
  @DisplayName("add: delete all inventory of book with id, expected success")
  void testDeleteWithBookId() throws Exception {
    doNothing().when(repositoryMock).deleteByIdBookId(1L);
    subject.delete(1L);
    verify(repositoryMock).deleteByIdBookId(anyLong());
  }

  @Test
  @DisplayName(
      "add: deleting a book with id which is not present, expected InventoryNotFoundException")
  void testDeleteWithNoInventoryPresent() {
    assertThrows(
        InventoryNotFoundException.class,
        () -> {
          doThrow(EmptyResultDataAccessException.class).when(repositoryMock).deleteByIdBookId(1L);
          subject.delete(1L);
          verify(repositoryMock).deleteByIdBookId(anyLong());
        });
  }

  @Test
  @DisplayName("add: delete all inventory of book with id & reference id, expected success")
  void testDeleteWithBookIdAndBookReference() throws Exception {
    doNothing().when(repositoryMock).deleteById(new InventoryId(1L, "abc"));
    subject.delete(1L, "abc");
    verify(repositoryMock).deleteById(any(InventoryId.class));
  }

  @Test
  @DisplayName(
      "add: deleting a book with id & reference id which is not present, expected InventoryNotFoundException")
  void testDeleteWithNoInventoryPresentOfBookReference() {
    assertThrows(
        InventoryNotFoundException.class,
        () -> {
          doThrow(EmptyResultDataAccessException.class)
              .when(repositoryMock)
              .deleteById(new InventoryId(1L, "abc"));
          subject.delete(1L, "abc");
          verify(repositoryMock).deleteById(any(InventoryId.class));
        });
  }

  private List<Inventory> createMockInventory(long id, boolean available) {
    return Arrays.asList(
        Inventory.builder()
            .id(new InventoryId(id, "abc"))
            .available(available)
            .categoryId(1)
            .build(),
        Inventory.builder()
            .id(new InventoryId(id, "pqr"))
            .available(available)
            .categoryId(1)
            .build());
  }
}
