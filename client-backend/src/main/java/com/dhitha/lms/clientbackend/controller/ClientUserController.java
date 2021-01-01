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
import org.springframework.security.core.Authentication;
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
@PreAuthorize("hasAuthority('USER')")
@RequiredArgsConstructor
public class ClientUserController {

  private final UserClient userClient;

  @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDTO> getTokenAuthenticatedUser(Authentication authentication) {
    UserDTO userDTO = (UserDTO)authentication.getPrincipal();
    return ResponseEntity.ok(userClient.get(userDTO.getId()));
  }
}
