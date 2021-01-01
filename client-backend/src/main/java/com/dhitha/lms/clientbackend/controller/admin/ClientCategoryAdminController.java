package com.dhitha.lms.clientbackend.controller.admin;

import com.dhitha.lms.clientbackend.client.BookClient;
import com.dhitha.lms.clientbackend.dto.CategoryDTO;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for book service for category for admins
 *
 * @author Dhiraj
 */
@RestController
@RequestMapping("/admin/categories")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class ClientCategoryAdminController {

  private final BookClient categoryClient;

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

  @PutMapping(
      value = "/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CategoryDTO> update(
      @PathVariable Integer id, @Valid @RequestBody CategoryDTO categoryDTO) {
    return ResponseEntity.ok(categoryClient.updateCategory(id, categoryDTO));
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    categoryClient.deleteCategory(id);
    return ResponseEntity.noContent().build();
  }
}
