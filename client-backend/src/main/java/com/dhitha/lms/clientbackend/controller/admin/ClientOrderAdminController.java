package com.dhitha.lms.clientbackend.controller.admin;

import com.dhitha.lms.clientbackend.client.OrderClient;
import com.dhitha.lms.clientbackend.dto.BookOrderDTO;
import com.dhitha.lms.clientbackend.dto.BookOrderHistoryDTO;
import com.dhitha.lms.clientbackend.util.OrderUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for Orders for admins
 *
 * @author Dhiraj
 */
@RestController
@RequestMapping("/admin/orders")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class ClientOrderAdminController {

  private final OrderClient client;

  private final OrderUtil orderUtil;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookOrderDTO> findById(@PathVariable Long id) {
    BookOrderDTO order = client.findById(id);
    order.setBookName(orderUtil.getAllBookNames().get(order.getBookId()));
    return ResponseEntity.ok(order);
  }

  @GetMapping(value = "/books/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllByBook(@PathVariable Long bookId) {
    List<BookOrderDTO> orders = client.findAllByBook(bookId);
    orderUtil.setBookNameForOrderList(orders);
    return ResponseEntity.ok(orders);
  }

  @GetMapping(value = "/overdue/return", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllReturnOverdue() {
    List<BookOrderDTO> orders = client.findAllReturnOverdue(null);
    orderUtil.setBookNameForOrderList(orders);
    return ResponseEntity.ok(orders);
  }

  @GetMapping(value = "/overdue/collect", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllCollectionOverdue() {
    List<BookOrderDTO> orders = client.findAllCollectionOverdue(null);
    orderUtil.setBookNameForOrderList(orders);
    return ResponseEntity.ok(orders);
  }

  @PutMapping(value = "/{id}/collect")
  public ResponseEntity<BookOrderDTO> markBookAsCollected(@PathVariable Long id) {
    BookOrderDTO order = client.collectBook(id);
    order.setBookName(orderUtil.getAllBookNames().get(order.getBookId()));
    return ResponseEntity.ok(order);
  }

  @PutMapping(value = "/{id}/return")
  public ResponseEntity<Void> markBookAsReturned(@PathVariable Long id) {
    client.returnBook(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllByUser(@PathVariable Long userId) {
    List<BookOrderDTO> orders = client.findAllByUser(userId);
    orderUtil.setBookNameForOrderList(orders);
    return ResponseEntity.ok(orders);
  }

  @GetMapping(value = "/users/{userId}/history", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderHistoryDTO>> findAllHistoryOfUser(@PathVariable Long userId) {
    List<BookOrderHistoryDTO> orderHistory = client.findAllHistoryOfUser(userId);
    orderHistory.forEach(
        bookOrderDTO ->
            bookOrderDTO.setBookName(orderUtil.getAllBookNames().get(bookOrderDTO.getBookId())));
    return ResponseEntity.ok(orderHistory);
  }

  @GetMapping(value = "/users/{userId}/overdue/return", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllReturnOverdueOfUser(@PathVariable Long userId) {
    List<BookOrderDTO> orders = client.findAllReturnOverdue(userId);
    orderUtil.setBookNameForOrderList(orders);
    return ResponseEntity.ok(orders);
  }

  @GetMapping(
      value = "/users/{userId}/overdue/collect",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllCollectionOverdueOfUser(
      @PathVariable Long userId) {
    List<BookOrderDTO> orders = client.findAllCollectionOverdue(userId);
    orderUtil.setBookNameForOrderList(orders);
    return ResponseEntity.ok(orders);
  }
}