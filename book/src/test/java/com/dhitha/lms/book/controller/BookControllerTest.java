package com.dhitha.lms.book.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dhitha.lms.book.dto.BookDTO;
import com.dhitha.lms.book.dto.CategoryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * Integration Tests {@link BookController} Depends on data-test.sql file
 *
 * @author Dhiraj
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class BookControllerTest {

  @Autowired private ObjectMapper objectMapper;

  @Autowired private MockMvc mockMvc;

  @Value("${lms.client.key}")
  private String apiKey;

  @Test
  @Order(0)
  void contextLoads() {}

  @Test
  @Order(1)
  void testGetAll() throws Exception {
    mockMvc
        .perform(get("/books/v1").header("lms-key", apiKey))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)));
  }

  @Test
  @Order(2)
  void testGetAllByAuthor() throws Exception {
    mockMvc
        .perform(get("/books/v1").param("author", "JK").header("lms-key", apiKey))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].author", is("JK")))
        .andExpect(jsonPath("$[1].author", is("JK")));
  }

  @Test
  @Order(3)
  void testGetAllByName() throws Exception {
    mockMvc
        .perform(get("/books/v1").param("name", "Data Structure").header("lms-key", apiKey))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].name", is("Data Structure and Algorithm")));
  }

  @Test
  @Order(4)
  void testGetAllByPublication() throws Exception {
    mockMvc
        .perform(get("/books/v1").param("publication", "WB").header("lms-key", apiKey))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  @Order(5)
  void testGetAllByCategory() throws Exception {
    mockMvc
        .perform(get("/books/v1").param("categoryId", "2").header("lms-key", apiKey))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  @Order(6)
  void testGetById() throws Exception {
    mockMvc
        .perform(get("/books/v1/{id}", 1).header("lms-key", apiKey))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("Data Structure and Algorithm")))
        .andExpect(jsonPath("$.author", is("DT")))
        .andExpect(jsonPath("$.publication", is("DTP")));
  }

  @Test
  @Order(7)
  void testGetByIdNotFound() throws Exception {
    mockMvc
        .perform(get("/books/v1/{id}", 200).header("lms-key", apiKey))
        .andExpect(status().isNotFound());
  }

  @Test
  @Order(8)
  void testPost() throws Exception {
    BookDTO bookDTO =
        BookDTO.builder()
            .name("test")
            .author("TT")
            .isbn("979-8013-9-9459-1")
            .publication("TTP")
            .category(new CategoryDTO(1, "TECHNOLOGY"))
            .pages(100)
            .build();
    mockMvc
        .perform(
            post("/books/v1")
                .header("lms-key", apiKey)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(bookDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(4)));
  }

  @Test
  @Order(9)
  void testPostInvalidRequest() throws Exception {
    BookDTO bookDTO = BookDTO.builder().name("test").build();
    mockMvc
        .perform(
            post("/books/v1")
                .header("lms-key", apiKey)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(bookDTO)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(10)
  void testPostInvalidISBN() throws Exception {
    BookDTO bookDTO =
        BookDTO.builder()
            .name("test")
            .author("TT")
            .isbn("98910")
            .publication("TTP")
            .category(new CategoryDTO(1, "TECHNOLOGY"))
            .pages(100)
            .build();
    mockMvc
        .perform(
            post("/books/v1")
                .header("lms-key", apiKey)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(bookDTO)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Order(11)
  void testPut() throws Exception {
    BookDTO bookDTO = BookDTO.builder().name("Data Structures and Algorithms").build();
    mockMvc
        .perform(
            put("/books/v1/{id}", 1)
                .header("lms-key", apiKey)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(bookDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("Data Structures and Algorithms")));
  }

  @Test
  @Order(12)
  void testDelete() throws Exception {
    mockMvc
        .perform(delete("/books/v1/{id}", 1).header("lms-key", apiKey))
        .andExpect(status().isNoContent());
  }

  @Test
  @Order(13)
  void testDeleteNotFound() throws Exception {
    mockMvc
        .perform(delete("/books/v1/{id}", 200).header("lms-key", apiKey))
        .andExpect(status().isNotFound());
  }

  @Test
  @Order(14)
  void testWithoutApiKey() throws Exception {
    mockMvc.perform(delete("/books/v1/{id}", 2)).andExpect(status().isForbidden());
  }
}
