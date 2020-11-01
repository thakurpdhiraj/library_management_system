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
import com.dhitha.lms.user.service.UserService;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests {@link UserController} Depends on the data.sql file
 *
 * <pre>
 * insert into role (id,name) values (null,'USER');
 * insert into role (id,name) values (null,'ADMIN');
 *
 * insert into user (id,name,email,account_non_expired,account_non_locked,created_at,credentials_non_expired,enabled,password,updated_at,username)
 *  values (null,'admin-lms','admin@lms.com',1,1,current_timestamp(),1,1,'$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a',current_timestamp(),'admin');
 *
 * insert into user_role(user_id,role_id) values (1,1);
 * </pre>
 *
 * @author Dhiraj
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("dev")
public class UserControllerTest {

  private final ObjectMapper objectMapper = new ObjectMapper();
  @Autowired private MockMvc mockMvc;
  @Autowired private UserService userService;

  @Test
  @Order(0)
  void contextLoads() {}

  @Test
  @Order(1)
  public void testGetUserNotFound() throws Exception {
    mockMvc.perform(get("/v1/{id}", 200)).andExpect(status().isNotFound());
  }

  @Test
  @Order(2)
  public void testGetUserFound() throws Exception {
    mockMvc
        .perform(get("/v1/{id}", 1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.username", is("admin")));
  }

  @Test
  @Order(3)
  public void testSaveUserValidationFail() throws Exception {
    UserDTO mock = UserDTO.builder().username("username").password("password").build();
    mockMvc
        .perform(
            post("/v1")
                .content(objectMapper.writeValueAsString(mock))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(4)
  public void testSaveUserOk() throws Exception {
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
                .content(objectMapper.writeValueAsString(mock))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(jsonPath("$.id", is(2)));
    objectMapper.enable(MapperFeature.USE_ANNOTATIONS);
  }

  @Test
  @Order(5)
  public void testPutUserNotFound() throws Exception {
    UserDTO mock = UserDTO.builder().build();
    mockMvc
        .perform(
            put("/v1/{id}", 200)
                .content(objectMapper.writeValueAsString(mock))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Order(6)
  public void testPutUserOk() throws Exception {
    UserDTO mock = UserDTO.builder().name("user-lms").build();
    mockMvc
        .perform(
            put("/v1/{id}", 2)
                .content(objectMapper.writeValueAsString(mock))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(2)))
        .andExpect(jsonPath("$.name", is("user-lms")))
        .andExpect(jsonPath("$.username", is("user")));
  }

  @Test
  @Order(7)
  public void testDeleteUserNotFound() throws Exception {
    mockMvc.perform(delete("/v1/{id}", 200)).andExpect(status().isNotFound());
  }

  @Test
  @Order(8)
  public void testDeleteUserOk() throws Exception {
    mockMvc.perform(delete("/v1/{id}", 2)).andExpect(status().isNoContent());
  }

  @Test
  @Order(9)
  public void testGetAll() throws Exception {
    mockMvc
        .perform(get("/v1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(1)));
  }

  @Test
  @Order(10)
  public void testGetByCredentials() throws Exception {
    objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
    UserDTO mock = UserDTO.builder().username("admin").password("pass").build();
    mockMvc
        .perform(
            post("/v1/validate")
                .content(objectMapper.writeValueAsString(mock))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)));
    objectMapper.enable(MapperFeature.USE_ANNOTATIONS);
  }

  @Test
  @Order(11)
  public void testGetByCredentialsUserNotFound() throws Exception {
    objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
    UserDTO mock = UserDTO.builder().username("user").password("pass").build();
    mockMvc
        .perform(
            post("/v1/validate")
                .content(objectMapper.writeValueAsString(mock))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound());
    objectMapper.enable(MapperFeature.USE_ANNOTATIONS);
  }

  @Test
  @Order(11)
  public void testGetByCredentialsBadRequest() throws Exception {
    objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
    UserDTO mock = UserDTO.builder().username(null).password("pass").build();
    mockMvc
        .perform(
            post("/v1/validate")
                .content(objectMapper.writeValueAsString(mock))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
    objectMapper.enable(MapperFeature.USE_ANNOTATIONS);
  }
}
