package com.dhitha.lms.auth.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dhitha.lms.auth.TestUtils;
import com.dhitha.lms.auth.client.UserClient;
import com.dhitha.lms.auth.dto.AuthRequestDTO;
import com.dhitha.lms.auth.dto.AuthResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration Tests for {@link AuthController}
 *
 * @author Dhiraj
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired private MockMvc mockMvc;

  @MockBean private UserClient mockUserClient;

  @Value("${lms.client.key}")
  private String apiKey;

  @Test
  @DisplayName("post, authenticateUser: valid input no api key, expected 403")
  void testAuthenticateWithoutApiKey() throws Exception {
    AuthRequestDTO mockRequest = new AuthRequestDTO("admin", "pass");
    when(mockUserClient.getByCredentials(mockRequest)).thenReturn(TestUtils.createMockUser());
    mockMvc
        .perform(
            post("/v1/token/authenticate")
                .content(objectMapper.writeValueAsString(mockRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("post, authenticateUser: valid input invalid api key, expected 403")
  void testAuthenticateInvalidApiKey() throws Exception {
    AuthRequestDTO mockRequest = new AuthRequestDTO("admin", "password");
    when(mockUserClient.getByCredentials(mockRequest)).thenThrow(FeignException.NotFound.class);
    mockMvc
        .perform(
            post("/v1/token/authenticate")
                .header("lms-key", "invalid")
                .content(objectMapper.writeValueAsString(mockRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("post, authenticateUser: valid input, expected 200")
  void testAuthenticateInvalidCredentials() throws Exception {
    AuthRequestDTO mockRequest = new AuthRequestDTO("admin", "password");
    when(mockUserClient.getByCredentials(mockRequest)).thenThrow(FeignException.NotFound.class);
    mockMvc
        .perform(
            post("/v1/token/authenticate")
                .header("lms-key", apiKey)
                .content(objectMapper.writeValueAsString(mockRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("post, call authenticate to get token, then verify the token, expected 200")
  void testAuthenticateAndVerifyToken() throws Exception {
    AuthRequestDTO mockRequest = new AuthRequestDTO("admin", "pass");
    when(mockUserClient.getByCredentials(mockRequest)).thenReturn(TestUtils.createMockUser());
    String authResponse =
        mockMvc
            .perform(
                post("/v1/token/authenticate")
                    .header("lms-key", apiKey)
                    .content(objectMapper.writeValueAsString(mockRequest))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andReturn()
            .getResponse()
            .getContentAsString();

    AuthResponseDTO authResponseDTO = objectMapper.readValue(authResponse, AuthResponseDTO.class);
    String token =
        authResponseDTO.getHeader()
            + "."
            + authResponseDTO.getPayload()
            + "."
            + authResponseDTO.getSignature();

    mockMvc
        .perform(
            post("/v1/token/verify")
                .header("lms-key", apiKey)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("post, verifyToken: invalid token header, expected 400")
  void testVerifyTokenExpiredIdToken() throws Exception {
    mockMvc
        .perform(
            post("/v1/token/verify")
                .header("lms-key", apiKey)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TestUtils.createExpiredIdToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("post, verifyToken: invalid token header, expected 400")
  void testVerifyTokenInvalidTokenHeader() throws Exception {
    mockMvc
        .perform(
            post("/v1/token/verify")
                .header("lms-key", apiKey)
                .header(HttpHeaders.AUTHORIZATION, "token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("post, verifyToken: invalid token, expected 400")
  void testVerifyTokenInvalidToken() throws Exception {
    mockMvc
        .perform(
            post("/v1/token/verify")
                .header("lms-key", apiKey)
                .header(HttpHeaders.AUTHORIZATION, "Bearer mock")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isUnauthorized());
  }
}
