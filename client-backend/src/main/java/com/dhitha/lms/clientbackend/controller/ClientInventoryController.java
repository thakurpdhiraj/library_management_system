package com.dhitha.lms.clientbackend.controller;

import com.dhitha.lms.clientbackend.client.InventoryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST for Inventory
 *
 * @author Dhiraj
 */
@RestController
@RequestMapping("/inventory/books")
@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
@RequiredArgsConstructor
public class ClientInventoryController {

  private final InventoryClient client;

  @GetMapping(value = "/{bookId}/count", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Long> getAvailableCount(@PathVariable Long bookId) {
    long count = client.getAvailableCountOfBook(bookId);
    return ResponseEntity.ok(count);
  }
}
