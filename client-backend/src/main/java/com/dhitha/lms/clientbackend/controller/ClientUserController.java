package com.dhitha.lms.clientbackend.controller;

import com.dhitha.lms.clientbackend.client.UserClient;
import com.dhitha.lms.clientbackend.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    UserDTO userDTO = (UserDTO) authentication.getPrincipal();
    return ResponseEntity.ok(userClient.get(userDTO.getId()));
  }

  @PutMapping(
      value = "/me",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDTO> updateTokenAuthenticatedUser(
      @RequestBody UserDTO userDTO, Authentication authentication) {
    UserDTO loggedInUser = (UserDTO) authentication.getPrincipal();
    loggedInUser.setEmail(userDTO.getEmail());
    loggedInUser.setName(userDTO.getName());
    return ResponseEntity.ok(userClient.update(loggedInUser.getId(), loggedInUser));
  }

  @PostMapping(
      value = "/change-password",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDTO> changeUserPassword(
      @RequestBody UserDTO userDTO, Authentication authentication) {
    //do some more thing with changing password
    UserDTO loggedInUser = (UserDTO) authentication.getPrincipal();
    Assert.notNull(userDTO.getPassword(), "Password cannot be empty");
    loggedInUser.setPassword(userDTO.getPassword());
    loggedInUser.setCredentialsNonExpired(true);
    return ResponseEntity.ok(userClient.update(loggedInUser.getId(), loggedInUser));
  }
}
