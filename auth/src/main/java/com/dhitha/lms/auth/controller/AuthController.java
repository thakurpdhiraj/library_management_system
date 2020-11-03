package com.dhitha.lms.auth.controller;

import com.dhitha.lms.auth.dto.AuthRequestDTO;
import com.dhitha.lms.auth.dto.AuthResponseDTO;
import com.dhitha.lms.auth.error.GenericException;
import com.dhitha.lms.auth.service.AuthService;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for Auth Service
 *
 * @author Dhiraj
 */
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping(
      value = "/authenticate",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthResponseDTO> authenticateUser(
      @RequestBody @Valid AuthRequestDTO authDTO) throws GenericException {
    return ResponseEntity.ok().body(authService.authenticate(authDTO));
  }

  @PostMapping(
      value = "/token/verify",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> verifyToken(@NotEmpty @RequestBody Map<String, String> tokenMap)
      throws GenericException {
    String token = tokenMap.get("token");
    Assert.notNull(token, "token cannot be null");
    authService.verifyToken(token);
    return ResponseEntity.noContent().build();
  }
}
