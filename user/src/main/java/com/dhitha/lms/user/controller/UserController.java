package com.dhitha.lms.user.controller;

import com.dhitha.lms.user.dto.UserDTO;
import com.dhitha.lms.user.error.GenericException;
import com.dhitha.lms.user.error.UserNotFoundException;
import com.dhitha.lms.user.service.UserService;
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
 * REST Controller for {@link UserDTO}
 *
 * @author Dhiraj
 */
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping(
      value = "/validate",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDTO> getByCredentials(@RequestBody UserDTO user)
      throws UserNotFoundException {
    UserDTO dbUser = userService.findByCredentials(user.getUsername(), user.getPassword());
    return ResponseEntity.ok(dbUser);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<UserDTO>> getAll() {
    return ResponseEntity.ok(userService.findAll());
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDTO> get(@PathVariable Long id) throws UserNotFoundException {
    UserDTO user = userService.findById(id);
    return ResponseEntity.ok(user);
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDTO> save(@RequestBody @Valid UserDTO user) throws GenericException {
    UserDTO dbUser = userService.save(user);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(dbUser.getId())
            .toUri();
    return ResponseEntity.created(uri).body(dbUser);
  }

  @PutMapping(
      value = "/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO user)
      throws UserNotFoundException {
    user.setId(id);
    return ResponseEntity.ok(userService.update(user));
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) throws UserNotFoundException {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
