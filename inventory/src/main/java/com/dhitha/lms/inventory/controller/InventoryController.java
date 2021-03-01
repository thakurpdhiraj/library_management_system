package com.dhitha.lms.inventory.controller;

import com.dhitha.lms.inventory.dto.InventoryDTO;
import com.dhitha.lms.inventory.error.GenericException;
import com.dhitha.lms.inventory.error.InventoryNotFoundException;
import com.dhitha.lms.inventory.service.InventoryService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST for Inventory
 *
 * @author Dhiraj
 */
@RestController
@RequestMapping("/v1/books")
@RequiredArgsConstructor
public class InventoryController {

  private final InventoryService inventoryService;

  @GetMapping(value = "/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<InventoryDTO>> getAll(@PathVariable Long bookId) {
    return ResponseEntity.ok(inventoryService.findAllBook(bookId));
  }

  @GetMapping(value = "/{bookId}/count", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Long> getAvailableCount(@PathVariable Long bookId) {
    long count = inventoryService.getAvailableCount(bookId);
    return ResponseEntity.ok(count);
  }

  @PostMapping(value = "/{bookId}/order", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<InventoryDTO> orderIfAvailable(@PathVariable Long bookId)
      throws InventoryNotFoundException {
    InventoryDTO inventoryDTO = inventoryService.orderBookIfAvailable(bookId);
    return ResponseEntity.ok(inventoryDTO);
  }

  @PostMapping(value = "/{bookId}/return", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Boolean> returnBook(
      @PathVariable Long bookId, @RequestParam String bookReferenceId) {
    return ResponseEntity.ok(inventoryService.returnBook(bookId, bookReferenceId));
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<InventoryDTO>> save(
      @Valid @RequestBody InventoryDTO inventoryDTO,
      @RequestHeader(value = "count", required = false) Integer count)
      throws GenericException {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(inventoryService.add(inventoryDTO, count));
  }

  @DeleteMapping(value = "/{bookId}")
  public ResponseEntity<Void> delete(
      @PathVariable Long bookId, @RequestParam(required = false) List<String> bookReferenceId)
      throws InventoryNotFoundException {
    if (bookReferenceId == null || bookReferenceId.isEmpty()) {
      inventoryService.delete(bookId);
    } else {
      inventoryService.delete(bookId, bookReferenceId);
    }
    return ResponseEntity.noContent().build();
  }
}
