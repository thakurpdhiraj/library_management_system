package com.dhitha.lms.clientbackend.controller;

import com.dhitha.lms.clientbackend.client.InventoryClient;
import com.dhitha.lms.clientbackend.dto.InventoryDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST for Inventory
 *
 * @author Dhiraj
 */
@RestController
@RequestMapping("/inventory/books")
@RequiredArgsConstructor
public class ClientInventoryController {

  private final InventoryClient client;

  @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
  @GetMapping(value = "/{bookId}/count", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Long> getAvailableCount(@PathVariable Long bookId) {
    long count = client.getAvailableCountOfBook(bookId);
    return ResponseEntity.ok(count);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping(value = "/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<InventoryDTO>> getAll(@PathVariable Long bookId) {
    return ResponseEntity.ok(client.getAllInventoryOfBook(bookId));
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> save(@RequestBody InventoryDTO inventoryDTO) {
    client.save(inventoryDTO);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping(value = "/{bookId}")
  public ResponseEntity<Void> delete(
      @PathVariable Long bookId, @RequestParam(required = false) String bookReferenceId) {
    if (StringUtils.isEmpty(bookReferenceId)) {
      client.delete(bookId, null);
    } else {
      client.delete(bookId, bookReferenceId);
    }
    return ResponseEntity.noContent().build();
  }
}
