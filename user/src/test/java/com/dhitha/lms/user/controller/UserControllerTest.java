package com.dhitha.lms.user.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dhitha.lms.user.dto.UserDTO;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests {@link UserController}. Depends on the data-test.sql file
 *
 * @author Dhiraj
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class UserControllerTest {

  private final ObjectMapper objectMapper = new ObjectMapper();
  @Autowired private MockMvc mockMvc;

  @Value("${lms.client.key}")
  private String apiKey;

  @Test
  @DisplayName("Test if context loads")
  @Order(0)
  void contextLoads() {}

  @Test
  @DisplayName("get, find by id: invalid data, expected 404")
  @Order(1)
  void testGetUserNotFound() throws Exception {
    mockMvc
        .perform(get("/v1/{id}", 200).header("lms-key", apiKey))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("get, find by id: valid data, expected 200")
  @Order(2)
  void testGetUserFound() throws Exception {
    mockMvc
        .perform(get("/v1/{id}", 1).header("lms-key", apiKey))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.username", is("admin")));
  }

  @Test
  @DisplayName("post, save: invalid input data, expected 400")
  @Order(3)
  void testSaveUserValidationFail() throws Exception {
    UserDTO mock = UserDTO.builder().username("username").password("password").build();
    mockMvc
        .perform(
            post("/v1")
                .header("lms-key", apiKey)
                .content(objectMapper.writeValueAsString(mock))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("post, save: valid input data, expected 201 with Location Header")
  @Order(4)
  void testSaveUserOk() throws Exception {
    objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
    UserDTO mock =
        UserDTO.builder()
            .name("name")
            .username("user")
            .password("password")
            .email("email")
            .userRoles(Collections.singletonList("USER"))
            .build();
    mockMvc
        .perform(
            post("/v1")
                .header("lms-key", apiKey)
                .content(objectMapper.writeValueAsString(mock))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(jsonPath("$.id", is(2)));
    objectMapper.enable(MapperFeature.USE_ANNOTATIONS);
  }

  @Test
  @DisplayName("put, update: invalid user id, expected 404")
  @Order(5)
  void testPutUserNotFound() throws Exception {
    UserDTO mock = UserDTO.builder().build();
    mockMvc
        .perform(
            put("/v1/{id}", 200)
                .header("lms-key", apiKey)
                .content(objectMapper.writeValueAsString(mock))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("put, update: valid user id & data, expected 200")
  @Order(6)
  void testPutUserOk() throws Exception {
    UserDTO mock = UserDTO.builder().name("user-lms").build();
    mockMvc
        .perform(
            put("/v1/{id}", 2)
                .header("lms-key", apiKey)
                .content(objectMapper.writeValueAsString(mock))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(2)))
        .andExpect(jsonPath("$.name", is("user-lms")))
        .andExpect(jsonPath("$.username", is("user")));
  }

  @Test
  @DisplayName("delete, delete one: invalid user id, expected 404")
  @Order(7)
  void testDeleteUserNotFound() throws Exception {
    mockMvc
        .perform(delete("/v1/{id}", 200).header("lms-key", apiKey))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("delete, delete one: valid user id, expected 204")
  @Order(8)
  void testDeleteUserOk() throws Exception {
    mockMvc
        .perform(delete("/v1/{id}", 2).header("lms-key", apiKey))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("get, find all: no input, expected 200, data as array")
  @Order(9)
  void testGetAll() throws Exception {
    mockMvc
        .perform(get("/v1").header("lms-key", apiKey))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(1)));
  }

  @Test
  @DisplayName("post, find by credentials: valid input, expected 200")
  @Order(10)
  void testGetByCredentials() throws Exception {
    objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
    UserDTO mock = UserDTO.builder().username("admin").password("pass").build();
    mockMvc
        .perform(
            post("/v1/validate")
                .header("lms-key", apiKey)
                .content(objectMapper.writeValueAsString(mock))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)));
    objectMapper.enable(MapperFeature.USE_ANNOTATIONS);
  }

  @Test
  @DisplayName("post, find by credentials: wrong credentials, expected 404")
  @Order(11)
  void testGetByCredentialsUserNotFound() throws Exception {
    objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
    UserDTO mock = UserDTO.builder().username("user").password("pass").build();
    mockMvc
        .perform(
            post("/v1/validate")
                .header("lms-key", apiKey)
                .content(objectMapper.writeValueAsString(mock))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound());
    objectMapper.enable(MapperFeature.USE_ANNOTATIONS);
  }

  @Test
  @DisplayName("post, find by credentials: invalid / null credentials, expected 400")
  @Order(11)
  void testGetByCredentialsBadRequest() throws Exception {
    objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
    UserDTO mock = UserDTO.builder().username(null).password("pass").build();
    mockMvc
        .perform(
            post("/v1/validate")
                .header("lms-key", apiKey)
                .content(objectMapper.writeValueAsString(mock))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
    objectMapper.enable(MapperFeature.USE_ANNOTATIONS);
  }

  @Test
  @DisplayName("get, test with no api key: no lms-key header, expected 403")
  @Order(12)
  void testWithoutApiKey() throws Exception {
    mockMvc.perform(get("/v1/{id}", 200)).andExpect(status().isForbidden());
  }
}
