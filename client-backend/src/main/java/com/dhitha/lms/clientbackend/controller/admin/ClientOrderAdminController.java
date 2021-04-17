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

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookOrderDTO> findById(@PathVariable Long id) {
    BookOrderDTO order = client.findById(id);
    return ResponseEntity.ok(order);
  }

  @GetMapping(value = "/books/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllByBook(@PathVariable Long bookId) {
    List<BookOrderDTO> orders = client.findAllByBook(bookId);
    return ResponseEntity.ok(orders);
  }

  @GetMapping(value = "/overdue/return", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllReturnOverdue() {
    List<BookOrderDTO> orders = client.findAllReturnOverdue(null);
    return ResponseEntity.ok(orders);
  }

  @GetMapping(value = "/overdue/collect", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllCollectionOverdue() {
    List<BookOrderDTO> orders = client.findAllCollectionOverdue(null);
    return ResponseEntity.ok(orders);
  }

  @PutMapping(value = "/{id}/collect")
  public ResponseEntity<BookOrderDTO> markBookAsCollected(@PathVariable Long id) {
    BookOrderDTO order = client.collectBook(id);
    return ResponseEntity.ok(order);
  }

  @PutMapping(value = "/{id}/return")
  public ResponseEntity<BookOrderDTO> markBookAsReturned(@PathVariable Long id) {
    BookOrderDTO order = client.returnBook(id);
    System.out.println("Order:::::"+order);
    return ResponseEntity.ok(order);
  }

  @GetMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllByUser(@PathVariable Long userId) {
    List<BookOrderDTO> orders = client.findAllByUser(userId);
    return ResponseEntity.ok(orders);
  }

  @GetMapping(value = "/users/{userId}/history", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderHistoryDTO>> findAllHistoryOfUser(@PathVariable Long userId) {
    List<BookOrderHistoryDTO> orderHistory = client.findAllHistoryOfUser(userId);
    return ResponseEntity.ok(orderHistory);
  }

  @GetMapping(value = "/users/{userId}/overdue/return", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllReturnOverdueOfUser(@PathVariable Long userId) {
    List<BookOrderDTO> orders = client.findAllReturnOverdue(userId);
    return ResponseEntity.ok(orders);
  }

  @GetMapping(
      value = "/users/{userId}/overdue/collect",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllCollectionOverdueOfUser(
      @PathVariable Long userId) {
    List<BookOrderDTO> orders = client.findAllCollectionOverdue(userId);
    return ResponseEntity.ok(orders);
  }
}
