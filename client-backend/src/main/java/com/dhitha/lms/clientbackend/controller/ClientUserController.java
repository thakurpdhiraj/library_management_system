package com.dhitha.lms.clientbackend.controller;

import com.dhitha.lms.clientbackend.client.UserClient;
import com.dhitha.lms.clientbackend.dto.UserDTO;
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
 * REST controller for user service
 *
 * @author Dhiraj
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class ClientUserController {

  private final UserClient userClient;

  @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<UserDTO>> getAll() {
    return ResponseEntity.ok(userClient.getAll());
  }

  @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDTO> get(@PathVariable Long id) {
    return ResponseEntity.ok(userClient.get(id));
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDTO> save(@RequestBody @Valid UserDTO userDTO) {
    UserDTO dbUser = userClient.save(userDTO);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(dbUser.getId())
            .toUri();
    return ResponseEntity.created(uri).body(dbUser);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping(
      value = "/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO user) {
    return ResponseEntity.ok(userClient.update(id, user));
  }


  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    userClient.delete(id);
    return ResponseEntity.noContent().build();
  }
}
