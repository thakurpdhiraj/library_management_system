package com.dhitha.lms.clientbackend.controller;

import com.dhitha.lms.clientbackend.client.BookClient;
import com.dhitha.lms.clientbackend.dto.BookDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for Books
 *
 * @author Dhiraj
 */
@RestController
@RequestMapping("/books")
@PreAuthorize("hasAuthority('USER')")
@RequiredArgsConstructor
public class ClientBookController {

  private final BookClient bookClient;

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

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(bookClient.getById(id));
  }
}
