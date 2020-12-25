package com.dhitha.lms.clientbackend.controller;

import com.dhitha.lms.clientbackend.client.BookClient;
import com.dhitha.lms.clientbackend.dto.BookDTO;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
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
 * REST controller for Books
 *
 * @author Dhiraj
 */
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class ClientBookController {

  private final BookClient bookClient;

  @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookDTO>> getAllBooks(
      @RequestParam(value = "author", required = false) String author) {
    List<BookDTO> bookList;
    if (StringUtils.isEmpty(author)) {
      bookList = bookClient.getAllBooks(null);
    } else {
      bookList = bookClient.getAllBooks(author);
    }
    return ResponseEntity.ok(bookList);
  }

  @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(bookClient.getById(id));
  }

  @PreAuthorize("hasAuthority('ADMIN')")
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

  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping(
      value = "/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookDTO> update(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
    return ResponseEntity.ok(bookClient.update(id, bookDTO));
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    bookClient.delete(id);
    return ResponseEntity.noContent().build();
  }
}
