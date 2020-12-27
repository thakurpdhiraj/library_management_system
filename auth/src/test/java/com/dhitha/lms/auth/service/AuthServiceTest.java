package com.dhitha.lms.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dhitha.lms.auth.TestUtils;
import com.dhitha.lms.auth.client.UserClient;
import com.dhitha.lms.auth.dto.AuthRequestDTO;
import com.dhitha.lms.auth.dto.AuthResponseDTO;
import com.dhitha.lms.auth.dto.UserDTO;
import com.dhitha.lms.auth.error.GenericException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTClaimsSet.Builder;
import feign.FeignException;
import feign.FeignException.FeignClientException;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link AuthService}
 *
 * @author Dhiraj
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock private UserClient userClient;

  @Mock private TokenService tokenService;

  private AuthService subject;

  @BeforeEach
  void init() {
    subject = new AuthServiceImpl(userClient, tokenService);
  }

  @Test
  @DisplayName("authenticate: valid input, expected success")
  void testAuthenticateHappy() throws Exception {
    AuthRequestDTO reqMock = createMockReq();
    UserDTO userDTO = TestUtils.createMockUser();
    when(userClient.getByCredentials(reqMock)).thenReturn(userDTO);
    when(tokenService.generateIdToken(userDTO)).thenReturn("abc.xyz.pqr");

    AuthResponseDTO result = subject.authenticate(reqMock);
    assertEquals("abc", result.getHeader());
    assertEquals("xyz", result.getPayload());
    assertEquals("pqr", result.getSignature());
    assertEquals(userDTO, result.getUserDTO());

    verify(userClient).getByCredentials(reqMock);
    verify(tokenService).generateIdToken(userDTO);
  }

  @Test
  @DisplayName("authenticate: invalid credentials input, expected GenericException")
  void testAuthenticateWithInvalidCredentials() throws Exception {
    assertThrows(
        GenericException.class,
        () -> {
          AuthRequestDTO reqMock = createMockReq();
          when(userClient.getByCredentials(reqMock)).thenThrow(FeignException.NotFound.class);

          subject.authenticate(reqMock);
        });
    verify(userClient).getByCredentials(any(AuthRequestDTO.class));
    verify(tokenService, never()).generateIdToken(any(UserDTO.class));
  }

  @Test
  @DisplayName("authenticate: valid input unexpected client exception, expected GenericException")
  void testAuthenticateWithUnexpectedClientException() throws Exception {
    assertThrows(
        GenericException.class,
        () -> {
          AuthRequestDTO reqMock = createMockReq();
          when(userClient.getByCredentials(reqMock)).thenThrow(FeignClientException.class);

          subject.authenticate(reqMock);
        });
    verify(userClient).getByCredentials(any(AuthRequestDTO.class));
    verify(tokenService, never()).generateIdToken(any(UserDTO.class));
  }

  @Test
  @DisplayName("authenticate: valid input exception on token generation, expected GenericException")
  void testAuthenticateWithTokenGenerationError() throws Exception {
    assertThrows(
        GenericException.class,
        () -> {
          AuthRequestDTO reqMock = createMockReq();
          UserDTO userDTO = TestUtils.createMockUser();
          when(userClient.getByCredentials(reqMock)).thenReturn(userDTO);
          when(tokenService.generateIdToken(userDTO)).thenThrow(GenericException.class);

          subject.authenticate(reqMock);
        });
    verify(userClient).getByCredentials(any(AuthRequestDTO.class));
    verify(tokenService).generateIdToken(any(UserDTO.class));
  }

  @Test
  @DisplayName(
      "authenticate: valid input invalid token results NoSuchElementException, expected GenericException")
  void testAuthenticateWithInvalidTokenGeneration() throws Exception {
    assertThrows(
        GenericException.class,
        () -> {
          AuthRequestDTO reqMock = createMockReq();
          UserDTO userDTO = TestUtils.createMockUser();
          when(userClient.getByCredentials(reqMock)).thenReturn(userDTO);
          when(tokenService.generateIdToken(userDTO)).thenReturn("abc.xyz");

          subject.authenticate(reqMock);
        });

    verify(userClient).getByCredentials(any(AuthRequestDTO.class));
    verify(tokenService).generateIdToken(any(UserDTO.class));
  }

  @Test
  @DisplayName("verifyToken: valid input, expected success")
  void testVerifyTokenHappy() throws Exception {
    JWTClaimsSet mockClaimResult = TestUtils.createMockClaim();
    UserDTO expectedUserResult = TestUtils.createMockUser();
    when(tokenService.verifyToken("token")).thenReturn(mockClaimResult);
    when(tokenService.generateIdToken(any(UserDTO.class), any(Date.class)))
        .thenReturn("abc.xyz.pqr");

    AuthResponseDTO result = subject.verifyToken("token");
    assertEquals("abc", result.getHeader());
    assertEquals("xyz", result.getPayload());
    assertEquals("pqr", result.getSignature());
    assertEquals(expectedUserResult, result.getUserDTO());
    verify(tokenService).verifyToken("token");
    verify(tokenService).generateIdToken(any(UserDTO.class), any(Date.class));
  }

  @Test
  @DisplayName("verifyToken: invalid input, expected GenericException")
  void testVerifyTokenInvalidToken() throws Exception {

    assertThrows(
        GenericException.class,
        () -> {
          when(tokenService.verifyToken("token")).thenThrow(GenericException.class);
          subject.verifyToken("token");
        });

    verify(tokenService).verifyToken("token");
    verify(tokenService, never()).generateIdToken(any(UserDTO.class), any(Date.class));
  }

  @Test
  @DisplayName(
      "verifyToken: invalid type of claim eg(accountNonExpired should be boolean not string), expected GenericException")
  void testVerifyTokenInvalidClaim() throws Exception {

    assertThrows(
        GenericException.class,
        () -> {
          JWTClaimsSet mockClaimResult =
              new Builder().subject("1").claim("accountNonExpired", "true").build();
          when(tokenService.verifyToken("token")).thenReturn(mockClaimResult);
          subject.verifyToken("token");
        });

    verify(tokenService).verifyToken("token");
    verify(tokenService, never()).generateIdToken(any(UserDTO.class), any(Date.class));
  }

  @Test
  @DisplayName(
      "verifyToken: invalid token results NoSuchElementException, expected GenericException")
  void testVerifyTokenInvalidTokenGeneration() throws Exception {

    assertThrows(
        GenericException.class,
        () -> {
          JWTClaimsSet mockClaimResult = TestUtils.createMockClaim();
          when(tokenService.verifyToken("token")).thenReturn(mockClaimResult);
          when(tokenService.generateIdToken(any(UserDTO.class), any(Date.class)))
              .thenReturn("abc.xyz");
          subject.verifyToken("token");
        });
    verify(tokenService).verifyToken("token");
    verify(tokenService).generateIdToken(any(UserDTO.class), any(Date.class));
  }

  private AuthRequestDTO createMockReq() {
    return new AuthRequestDTO("user", "pass");
  }
}
