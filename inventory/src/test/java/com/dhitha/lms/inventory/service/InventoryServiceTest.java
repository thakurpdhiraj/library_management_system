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
import com.dhitha.lms.inventory.error.InventoryNotFoundException;
import com.dhitha.lms.inventory.repository.InventoryRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
 * Unit tests for {@link InventoryService}
 *
 * @author Dhiraj
 */
@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

  private final ModelMapper modelMapper = new ModelMapper();
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
        });
    verify(repositoryMock).findByIdBookId(1L);
    verify(repositoryMock, never()).updateAvailability(any(), any(), eq(false));
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
        });
    verify(repositoryMock).findByIdBookId(1L);
    verify(repositoryMock, never()).updateAvailability(anyLong(), anyString(), eq(false));
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
    when(repositoryMock.saveAll(any())).thenReturn(Collections.singletonList(mockInventory));
    subject.add(InventoryDTO.builder().bookId(1L).isbn("abc").build(), 1);
    verify(repositoryMock).saveAll(any());
  }

  @Test
  @DisplayName("delete: delete all inventory of book with id, expected success")
  void testDeleteWithBookId() throws Exception {
    when(repositoryMock.findByIdBookId(1L))
        .thenReturn(Collections.singletonList(Inventory.builder().available(true).build()));
    doNothing().when(repositoryMock).deleteByIdBookId(1L);
    subject.delete(1L);
    verify(repositoryMock).findByIdBookId(anyLong());
    verify(repositoryMock).deleteByIdBookId(anyLong());
  }

  @Test
  @DisplayName(
      "delete: deleting a book with id which is not present, expected InventoryNotFoundException")
  void testDeleteWithNoInventoryPresent() throws Exception{
    when(repositoryMock.findByIdBookId(1L)).thenReturn(Collections.emptyList());
    subject.delete(1L);
    verify(repositoryMock).findByIdBookId(anyLong());
    verify(repositoryMock, never()).deleteByIdBookId(anyLong());
  }

  @Test
  @DisplayName(
      "delete: deleting a book with id which is not available / loaned, expected InventoryNotFoundException")
  void testDeleteWithNoInventoryAvailable() {
    when(repositoryMock.findByIdBookId(1L))
        .thenReturn(Collections.singletonList(Inventory.builder().available(false).build()));
    assertThrows(
        InventoryNotFoundException.class,
        () -> subject.delete(1L));
    verify(repositoryMock).findByIdBookId(anyLong());
    verify(repositoryMock, never()).deleteByIdBookId(anyLong());
  }

  @Test
  @DisplayName(
      "delete: delete inventory of book with id & reference id which is available / not loaned, expected success")
  void testDeleteWithBookIdAndBookReferenceAvailable() throws Exception {
    when(repositoryMock.findByIdBookIdAndIdBookReferenceId(1L, "abc"))
        .thenReturn(Optional.of(Inventory.builder().available(true).build()));
    doNothing().when(repositoryMock).deleteByIdBookIdAndIdBookReferenceId(1L, "abc");
    subject.delete(1L, Collections.singletonList("abc"));
    verify(repositoryMock).findByIdBookIdAndIdBookReferenceId(anyLong(), anyString());
    verify(repositoryMock).deleteByIdBookIdAndIdBookReferenceId(anyLong(), anyString());
  }

  @Test
  @DisplayName(
      "delete: delete all inventory of book with id & reference id with some book not available / loaned, expected exception")
  void testDeleteWithBookIdAndBookReferenceNotAvailable() {
    when(repositoryMock.findByIdBookIdAndIdBookReferenceId(1L, "abc"))
        .thenReturn(Optional.of(Inventory.builder().available(false).build()));
    assertThrows(
        InventoryNotFoundException.class,
        () -> subject.delete(1L, Collections.singletonList("abc")));
    verify(repositoryMock).findByIdBookIdAndIdBookReferenceId(anyLong(), anyString());
    verify(repositoryMock, never()).deleteByIdBookIdAndIdBookReferenceId(anyLong(), anyString());
  }

  @Test
  @DisplayName(
      "add: deleting a book with id & reference id which is not present, expected InventoryNotFoundException")
  void testDeleteWithNoInventoryPresentOfBookReference() {
    when(repositoryMock.findByIdBookIdAndIdBookReferenceId(1L, "abc"))
        .thenReturn(Optional.empty());
    assertThrows(
        InventoryNotFoundException.class,
        () -> subject.delete(1L, Collections.singletonList("abc")));
    verify(repositoryMock).findByIdBookIdAndIdBookReferenceId(anyLong(), anyString());
    verify(repositoryMock, never()).deleteByIdBookIdAndIdBookReferenceId(anyLong(), anyString());
  }

  private List<Inventory> createMockInventory(long id, boolean available) {
    return Arrays.asList(
        Inventory.builder()
            .id(new InventoryId(id, "isbn", "abc"))
            .available(available)
            .categoryId(1)
            .build(),
        Inventory.builder()
            .id(new InventoryId(id, "isbn", "pqr"))
            .available(available)
            .categoryId(1)
            .build());
  }
}
