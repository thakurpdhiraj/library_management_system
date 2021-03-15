package com.dhitha.lms.book.controller;

import com.dhitha.lms.book.dto.BookDTO;
import com.dhitha.lms.book.dto.CategoryDTO;
import com.dhitha.lms.book.error.BookNotFoundException;
import com.dhitha.lms.book.service.BookService;
import com.dhitha.lms.book.service.CategoryService;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
 * REST Controller for {@link com.dhitha.lms.book.entity.Book}
 *
 * @author Dhiraj
 */
@RestController
@RequestMapping("/books/v1")
@RequiredArgsConstructor
public class BookController {

  private final BookService bookService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookDTO>> getAllBooks(BookDTO bookDTO,
      @RequestParam(value = "categoryId", required = false) Integer categoryId) {
    bookDTO.setCategory(new CategoryDTO(categoryId, null));
    List<BookDTO> bookList = bookService.findAll(bookDTO);
    return ResponseEntity.ok(bookList);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookDTO> getById(@PathVariable Long id) throws BookNotFoundException {
    return ResponseEntity.ok(bookService.findById(id));
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookDTO> save(@RequestBody @Valid BookDTO bookDTO) {

    BookDTO savedBook = bookService.save(bookDTO);
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
  public ResponseEntity<BookDTO> update(@PathVariable Long id, @RequestBody BookDTO bookDTO)
      throws BookNotFoundException {
    bookDTO.setId(id);
    return ResponseEntity.ok(bookService.update(bookDTO));
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) throws BookNotFoundException {
    bookService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
