package com.dhitha.lms.clientbackend.controller;

import com.dhitha.lms.clientbackend.client.BookClient;
import com.dhitha.lms.clientbackend.dto.CategoryDTO;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for book service for category
 *
 * @author Dhiraj
 */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class ClientCategoryController {

  private final BookClient categoryClient;

  @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<CategoryDTO>> findAll() {
    return ResponseEntity.ok(categoryClient.findAllCategory());
  }

  @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CategoryDTO> findById(@PathVariable Integer id) {
    return ResponseEntity.ok(categoryClient.findCategoryById(id));
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CategoryDTO> save(@Valid @RequestBody CategoryDTO categoryDTO) {
    CategoryDTO savedCategory = categoryClient.saveCategory(categoryDTO);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedCategory.getId())
            .toUri();
    return ResponseEntity.created(uri).body(savedCategory);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping(
      value = "/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CategoryDTO> update(
      @PathVariable Integer id, @Valid @RequestBody CategoryDTO categoryDTO) {
    return ResponseEntity.ok(categoryClient.updateCategory(id, categoryDTO));
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    categoryClient.deleteCategory(id);
    return ResponseEntity.noContent().build();
  }
}
