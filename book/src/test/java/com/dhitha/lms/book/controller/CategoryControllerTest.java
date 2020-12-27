package com.dhitha.lms.book.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
 * Integration Tests for {@link CategoryController}
 *
 * @author Dhiraj
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class CategoryControllerTest {

  @Autowired private MockMvc mockMvc;

  @Value("${lms.client.key}")
  private String apiKey;

  @Test
  @Order(0)
  void testFindAll() throws Exception {
    mockMvc
        .perform(
            get("/categories/v1")
                .header("lms-key", apiKey)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  @Order(1)
  void testFindById() throws Exception {
    mockMvc
        .perform(
            get("/categories/v1/1")
                .header("lms-key", apiKey)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("TECHNOLOGY")));
  }

  @Test
  @Order(2)
  void testFindByIdNotFound() throws Exception {
    mockMvc
        .perform(
            get("/categories/v1/404")
                .header("lms-key", apiKey)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Order(3)
  void testSave() throws Exception {
    mockMvc
        .perform(
            post("/categories/v1")
                .header("lms-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"name\":\"CAT\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(3)))
        .andExpect(jsonPath("$.name", is("CAT")));
  }

  @Test
  @Order(4)
  void testUpdate() throws Exception {
    mockMvc
        .perform(
            put("/categories/v1/3")
                .header("lms-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"name\":\"DOG\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(3)))
        .andExpect(jsonPath("$.name", is("DOG")));
  }

  @Test
  @Order(5)
  void testDelete() throws Exception {
    mockMvc
        .perform(delete("/categories/v1/3").header("lms-key", apiKey))
        .andExpect(status().isNoContent());
  }

  @Test
  @Order(6)
  void testWithoutApiKey() throws Exception {
    mockMvc
        .perform(delete("/categories/v1/3"))
        .andExpect(status().isForbidden());
  }
}
