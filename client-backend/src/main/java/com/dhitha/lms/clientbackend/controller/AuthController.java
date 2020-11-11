package com.dhitha.lms.clientbackend.controller;

import com.dhitha.lms.clientbackend.client.AuthClient;
import com.dhitha.lms.clientbackend.dto.AuthRequestDTO;
import com.dhitha.lms.clientbackend.dto.AuthResponseDTO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication Controller
 *
 * @author Dhiraj
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthClient authClient;

  @PreAuthorize("permitAll()")
  @PostMapping(
      value = "/login",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity<?> authenticateUser(
      @Valid AuthRequestDTO authDTO, HttpServletResponse response) {
    AuthResponseDTO authResponseDTO = authClient.authenticateUser(authDTO);
    String idToken = authResponseDTO.getHeader() + "." + authResponseDTO.getPayload();
    response.addCookie(new Cookie("sid", idToken));
    Cookie signatureCooke = new Cookie("sig", authResponseDTO.getSignature());
    signatureCooke.setHttpOnly(true);
    response.addCookie(signatureCooke);
    return ResponseEntity.noContent().build();
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping(value = "/logout")
  public ResponseEntity<?> logoutUser(HttpServletResponse response) {
    Cookie sigCookie = new Cookie("sig", null);
    sigCookie.setMaxAge(0);
    sigCookie.setHttpOnly(true);
    Cookie sidCookie = new Cookie("sid", null);
    sidCookie.setMaxAge(0);

    response.addCookie(sigCookie);
    response.addCookie(sidCookie);
    return ResponseEntity.noContent().build();
  }
}
