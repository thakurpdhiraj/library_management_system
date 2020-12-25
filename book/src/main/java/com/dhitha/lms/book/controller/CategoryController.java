package com.dhitha.lms.book.controller;

import com.dhitha.lms.book.dto.CategoryDTO;
import com.dhitha.lms.book.error.CategoryNotFoundException;
import com.dhitha.lms.book.error.GenericException;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for {@link com.dhitha.lms.book.entity.Category}
 *
 * @author Dhiraj
 */
@RestController
@RequestMapping("/categories/v1")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<CategoryDTO>> findAll() {
    return ResponseEntity.ok(categoryService.findAll());
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CategoryDTO> findById(@PathVariable Integer id)
      throws CategoryNotFoundException {
    return ResponseEntity.ok(categoryService.findById(id));
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CategoryDTO> save(@Valid @RequestBody CategoryDTO categoryDTO)
      throws GenericException {
    CategoryDTO savedCategory = categoryService.save(categoryDTO);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedCategory.getId())
            .toUri();
    return ResponseEntity.created(uri).body(savedCategory);
  }

  @PutMapping(
      value = "/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CategoryDTO> update(
      @PathVariable Integer id, @Valid @RequestBody CategoryDTO categoryDTO)
      throws CategoryNotFoundException, GenericException {
    categoryDTO.setId(id);
    return ResponseEntity.ok(categoryService.update(categoryDTO));
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) throws CategoryNotFoundException {
    categoryService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
