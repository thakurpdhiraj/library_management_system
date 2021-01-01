package com.dhitha.lms.clientbackend.controller;

import com.dhitha.lms.clientbackend.client.AuthClient;
import com.dhitha.lms.clientbackend.dto.AuthRequestDTO;
import com.dhitha.lms.clientbackend.dto.AuthResponseDTO;
import com.dhitha.lms.clientbackend.dto.UserDTO;
import com.dhitha.lms.clientbackend.util.Constants;
import com.dhitha.lms.clientbackend.util.CookieUtil;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication Controller for Login
 *
 * <p>Logout is handled in {@link com.dhitha.lms.clientbackend.config.SecurityConfig}
 *
 * @author Dhiraj
 */
@RestController
@RequiredArgsConstructor
public class ClientAuthController {

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
    CookieUtil.addCookie(Constants.ID_COOKIE_NAME, idToken, false, response);
    CookieUtil.addCookie(
        Constants.SIGNATURE_COOKIE_NAME, authResponseDTO.getSignature(), true, response);
    UserDTO userDTO = authResponseDTO.getUserDTO();
    return ResponseEntity.ok(userDTO);
  }
}
