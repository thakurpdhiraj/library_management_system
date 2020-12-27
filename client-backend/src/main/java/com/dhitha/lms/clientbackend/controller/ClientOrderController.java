package com.dhitha.lms.clientbackend.controller;

import com.dhitha.lms.clientbackend.client.OrderClient;
import com.dhitha.lms.clientbackend.dto.BookOrderDTO;
import com.dhitha.lms.clientbackend.dto.BookOrderHistoryDTO;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Controller for Orders
 *
 * @author Dhiraj
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class ClientOrderController {

  private final OrderClient client;

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookOrderDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(client.findById(id));
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")

  @GetMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllByUser(@PathVariable Long userId) {
    return ResponseEntity.ok(client.findAllByUser(userId));
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
  @GetMapping(value = "/users/{userId}/history", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderHistoryDTO>> findAllHistoryOfUser(@PathVariable Long userId) {
    return ResponseEntity.ok(client.findAllHistoryOfUser(userId));
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping(value = "/books/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllByBook(@PathVariable Long bookId) {
    return ResponseEntity.ok(client.findAllByBook(bookId));
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
  @GetMapping(value = "/users/{userId}/overdue/return", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllReturnOverdueOfUser(@PathVariable Long userId) {
    return ResponseEntity.ok(client.findAllReturnOverdue(userId));
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping(value = "/overdue/return", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllReturnOverdue() {
    return ResponseEntity.ok(client.findAllReturnOverdue(null));
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
  @GetMapping(value = "/users/{userId}/overdue/collect", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllCollectionOverdueOfUser(
      @PathVariable Long userId) {
    return ResponseEntity.ok(client.findAllCollectionOverdue(userId));
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping(value = "/overdue/collect", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllCollectionOverdue() {
    return ResponseEntity.ok(client.findAllCollectionOverdue(null));
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookOrderDTO> orderBook(@RequestBody BookOrderDTO orderDTO) {
    BookOrderDTO savedOrder = client.orderBook(orderDTO);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedOrder.getId())
            .toUri();
    return ResponseEntity.created(uri).body(savedOrder);
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
  @PutMapping(value = "/{id}/collect")
  public ResponseEntity<BookOrderDTO> collectBook(@PathVariable Long id) {
    return ResponseEntity.ok(client.collectBook(id));
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
  @PutMapping(value = "/{id}/return")
  public ResponseEntity<Void> returnBook(@PathVariable Long id) {
    client.returnBook(id);
    return ResponseEntity.noContent().build();
  }
}
