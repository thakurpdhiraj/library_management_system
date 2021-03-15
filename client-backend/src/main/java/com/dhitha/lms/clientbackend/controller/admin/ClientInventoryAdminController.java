package com.dhitha.lms.clientbackend.controller.admin;

import com.dhitha.lms.clientbackend.client.InventoryClient;
import com.dhitha.lms.clientbackend.dto.InventoryDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * REST for Inventory for admins
 *
 * @author Dhiraj
 */
@RestController
@RequestMapping("/admin/inventory/books")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class ClientInventoryAdminController {

  private final InventoryClient client;

  @GetMapping(value = "/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<InventoryDTO>> getAll(@PathVariable Long bookId) {
    return ResponseEntity.ok(client.getAllInventoryOfBook(bookId));
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<InventoryDTO>> save(
      @RequestBody InventoryDTO inventoryDTO, @RequestHeader("count") Integer count) {
    return ResponseEntity.status(HttpStatus.CREATED).body(client.save(inventoryDTO, count));
  }

  @DeleteMapping(value = "/{bookId}")
  public ResponseEntity<Void> delete(
      @PathVariable Long bookId, @RequestParam(required = false) List<String> bookReferenceList) {
    client.delete(bookId, bookReferenceList);
    return ResponseEntity.noContent().build();
  }
}
