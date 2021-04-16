package com.dhitha.lms.clientbackend.controller.admin;

import com.dhitha.lms.clientbackend.client.BookClient;
import com.dhitha.lms.clientbackend.client.InventoryClient;
import com.dhitha.lms.clientbackend.dto.BookDTO;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for Books for admins
 *
 * @author Dhiraj
 */
@RestController
@RequestMapping("/admin/books")
@RequiredArgsConstructor
public class ClientBookAdminController {
  private final BookClient bookClient;
  private final InventoryClient inventoryClient;

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookDTO> save(@RequestBody @Valid BookDTO bookDTO) {
    BookDTO savedBook = bookClient.save(bookDTO);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedBook.getId())
            .toUri();
    return ResponseEntity.created(uri).body(savedBook);
  }

  @PutMapping(
      value = "/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookDTO> update(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
    return ResponseEntity.ok(bookClient.update(id, bookDTO));
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(
      @PathVariable Long id,
      @RequestParam(required = false, name = "bookReference") List<String> bookReferenceList) {
    inventoryClient.delete(id, bookReferenceList);
    if (Objects.isNull(bookReferenceList) || bookReferenceList.isEmpty()) {
      bookClient.delete(id);
    }
    return ResponseEntity.noContent().build();
  }
}
