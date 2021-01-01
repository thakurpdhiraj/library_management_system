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
@PreAuthorize("hasAuthority('USER')")
@RequiredArgsConstructor
public class ClientOrderController {

  private final OrderClient client;

  @GetMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllByUser(@PathVariable Long userId) {
    return ResponseEntity.ok(client.findAllByUser(userId));
  }

  @GetMapping(value = "/users/{userId}/history", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderHistoryDTO>> findAllHistoryOfUser(@PathVariable Long userId) {
    return ResponseEntity.ok(client.findAllHistoryOfUser(userId));
  }

  @GetMapping(value = "/users/{userId}/overdue/return", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllReturnOverdueOfUser(@PathVariable Long userId) {
    return ResponseEntity.ok(client.findAllReturnOverdue(userId));
  }

  @GetMapping(
      value = "/users/{userId}/overdue/collect",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllCollectionOverdueOfUser(
      @PathVariable Long userId) {
    return ResponseEntity.ok(client.findAllCollectionOverdue(userId));
  }

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
}
