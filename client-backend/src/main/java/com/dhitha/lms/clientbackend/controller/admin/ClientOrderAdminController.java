package com.dhitha.lms.clientbackend.controller.admin;

import com.dhitha.lms.clientbackend.client.OrderClient;
import com.dhitha.lms.clientbackend.dto.BookOrderDTO;
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
    return ResponseEntity.ok(client.findById(id));
  }

  @GetMapping(value = "/books/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllByBook(@PathVariable Long bookId) {
    return ResponseEntity.ok(client.findAllByBook(bookId));
  }

  @GetMapping(value = "/overdue/return", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllReturnOverdue() {
    return ResponseEntity.ok(client.findAllReturnOverdue(null));
  }

  @GetMapping(value = "/overdue/collect", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllCollectionOverdue() {
    return ResponseEntity.ok(client.findAllCollectionOverdue(null));
  }

  @PutMapping(value = "/{id}/collect")
  public ResponseEntity<BookOrderDTO> markBookAsCollected(@PathVariable Long id) {
    return ResponseEntity.ok(client.collectBook(id));
  }

  @PutMapping(value = "/{id}/return")
  public ResponseEntity<Void> markBookAsReturned(@PathVariable Long id) {
    client.returnBook(id);
    return ResponseEntity.noContent().build();
  }
}
