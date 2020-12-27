package com.dhitha.lms.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dhitha.lms.auth.TestUtils;
import com.dhitha.lms.auth.dto.UserDTO;
import com.dhitha.lms.auth.error.GenericException;
import com.nimbusds.jwt.JWTClaimsSet;
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Unit tests for {@link TokenService}
 *
 * @author Dhiraj
 */
@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

  @Spy private ResourceLoader resourceLoader = new DefaultResourceLoader();

  private TokenService subject;

  @BeforeEach
  void init() {
    subject = new TokenServiceImpl(resourceLoader);
  }

  @Test
  @DisplayName("generateIdToken: valid input, expected success")
  void testGenerateIdTokenSuccess() throws Exception {
    UserDTO userDTO = TestUtils.createMockUser();
    String result = subject.generateIdToken(userDTO);
    assertEquals(3, result.split("\\.").length);
    verify(resourceLoader).getResource("classpath:/certs/lms-private-key.pem");
  }

  @Test
  @DisplayName("generateIdToken: valid input incorrect private key, expected GenericException")
  void testGenerateIdTokenIncorrectKey() {
    assertThrows(
        GenericException.class,
        () -> {
          Resource mockResource =
          resourceLoader.getResource("classpath:/certs/incorrect-private-key.pem");
          when(resourceLoader.getResource(anyString()))
              .thenReturn(mockResource);
          UserDTO userDTO = TestUtils.createMockUser();
          subject.generateIdToken(userDTO);
        });

    verify(resourceLoader).getResource("classpath:/certs/lms-private-key.pem");
  }

  @Test
  @DisplayName("verifyToken: valid token, expected success")
  void testVerifyTokenSuccess() throws Exception {
    // Not really a UNIT test??!, can't figure out proper way :(
    UserDTO userDTO = TestUtils.createMockUser();
    String mockToken = subject.generateIdToken(userDTO);

    JWTClaimsSet result = subject.verifyToken(mockToken);
    assertEquals("http://localhost:8081", result.getIssuer());
    assertEquals("1", result.getSubject());
    assertEquals("name", result.getClaim("name"));
    assertEquals("ADMIN,USER", result.getClaim("roles"));
    verify(resourceLoader).getResource("classpath:/certs/lms-public-key.pem");
  }

  @Test
  @DisplayName("verifyToken: expired token, expected GenericException")
  void testVerifyTokenIncorrectKey() {
    assertThrows(
        GenericException.class,
        () -> {
          Resource mockResource =
              resourceLoader.getResource("classpath:/certs/incorrect-public-key.pem");
          when(resourceLoader.getResource(anyString()))
              .thenReturn(mockResource);
          String mockToken = "token";
          subject.verifyToken(mockToken);
        });

    verify(resourceLoader).getResource("classpath:/certs/lms-public-key.pem");
  }

  @Test
  @DisplayName("verifyToken: expired token, expected GenericException")
  void testVerifyTokenExpiredToken() {
    assertThrows(
        GenericException.class,
        () -> {
          String mockToken = TestUtils.createExpiredIdToken();
          subject.verifyToken(mockToken);
        });

    verify(resourceLoader).getResource("classpath:/certs/lms-public-key.pem");
  }
}
