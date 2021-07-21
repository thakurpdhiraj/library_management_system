package com.dhitha.lms.order.controller;

import com.dhitha.lms.order.dto.BookOrderDTO;
import com.dhitha.lms.order.dto.BookOrderHistoryDTO;
import com.dhitha.lms.order.error.GenericException;
import com.dhitha.lms.order.error.OrderNotFoundException;
import com.dhitha.lms.order.service.BookOrderHistoryService;
import com.dhitha.lms.order.service.BookOrderService;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Controller for Orders
 *
 * @author Dhiraj
 */
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class BookOrderController {

  private final BookOrderService bookOrderService;
  private final BookOrderHistoryService bookOrderHistoryService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookOrderDTO> findById(@PathVariable Long id)
      throws OrderNotFoundException {
    return ResponseEntity.ok(bookOrderService.findById(id));
  }

  @GetMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllByUser(@PathVariable Long userId) {
    return ResponseEntity.ok(bookOrderService.findAllByUser(userId));
  }

  @GetMapping(value = "/users/{userId}/history", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderHistoryDTO>> findAllHistoryOfUser(@PathVariable Long userId) {
    return ResponseEntity.ok(bookOrderHistoryService.findAllForUser(userId));
  }

  @GetMapping(value = "/books/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllByBook(@PathVariable Long bookId) {
    return ResponseEntity.ok(bookOrderService.findAllByBook(bookId));
  }

  @GetMapping(value = "/overdue/return", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllReturnOverdue(
      @RequestParam(value = "userId", required = false) Long userId) {
    if (userId == null) {
      return ResponseEntity.ok(bookOrderService.findAllReturnOverdue());
    } else {
      return ResponseEntity.ok(bookOrderService.findAllReturnOverdueForUser(userId));
    }
  }

  @GetMapping(value = "/overdue/collect", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllCollectionOverdue(
      @RequestParam(value = "userId", required = false) Long userId) {
    if (userId == null) {
      return ResponseEntity.ok(bookOrderService.findAllCollectionOverdue());
    } else {
      return ResponseEntity.ok(bookOrderService.findAllCollectionOverdueForUser(userId));
    }
  }

  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookOrderDTO> orderBook(@Valid @RequestBody BookOrderDTO orderDTO)
      throws GenericException {
    BookOrderDTO savedOrder = bookOrderService.orderBook(orderDTO);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedOrder.getId())
            .toUri();
    return ResponseEntity.created(uri).body(savedOrder);
  }

  @PutMapping(value = "/{id}/collect", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookOrderDTO> collectBook(@PathVariable Long id)
      throws OrderNotFoundException {
    return ResponseEntity.ok(bookOrderService.collectBook(id));
  }

  @PutMapping(value = "/{id}/return")
  public ResponseEntity<BookOrderDTO> returnBook(@PathVariable Long id)
      throws OrderNotFoundException, GenericException {
    return ResponseEntity.ok(bookOrderService.returnBook(id));
  }
}
