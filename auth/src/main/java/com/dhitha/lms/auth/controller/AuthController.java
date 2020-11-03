package com.dhitha.lms.auth.controller;

import com.dhitha.lms.auth.dto.AuthRequestDTO;
import com.dhitha.lms.auth.dto.AuthResponseDTO;
import com.dhitha.lms.auth.dto.UserDTO;
import com.dhitha.lms.auth.error.GenericException;
import com.dhitha.lms.auth.service.AuthService;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AuthController {
  private final AuthService authService;

  @PostMapping(
      value = "/authenticate",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthResponseDTO> authenticateUser(
      @RequestBody @Valid AuthRequestDTO authDTO) throws GenericException {
    log.info("Authenticating .....");
    AuthResponseDTO authenticate = authService.authenticate(authDTO);
    log.info("Authenticated : {}", authenticate);
    return ResponseEntity.ok().body(authenticate);
  }

  @PostMapping(
      value = "/token/verify",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDTO> verifyToken(@NotEmpty @RequestBody Map<String, String> tokenMap)
      throws GenericException {
    String token = tokenMap.get("token");
    Assert.notNull(token, "token cannot be null");
    return ResponseEntity.ok(authService.verifyToken(token));
  }
}
