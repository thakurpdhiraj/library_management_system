package com.dhitha.lms.inventory.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dhitha.lms.inventory.dto.InventoryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for {@link InventoryController}. Depends on /test/resources/data-test.sql file.
 *
 * @author Dhiraj
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class InventoryControllerTest {

  private static final String PATH = "/v1/books";
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @Value("${lms.client.key}")
  private String apiKey;

  @Test
  @Order(1)
  @DisplayName("get, getAll: find all inventory for a book id, expected 200")
  void testGetAll() throws Exception {
    mockMvc
        .perform(get(PATH + "/{bookId}", 2).header("lms-key", apiKey))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)));
  }

  @Test
  @Order(2)
  @DisplayName("get, getAll: find count of all inventory for a book id, expected 200")
  void testGetAvailableCount() throws Exception {
    checkCount(2, 3);
  }

  @Test
  @Order(3)
  @DisplayName("post, orderIfAvailable: order if available for a book id, expected 200")
  void testOrderIfAvailable() throws Exception {
    mockMvc
        .perform(
            post(PATH + "/{bookId}/order", 2)
                .header("lms-key", apiKey)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.bookId", is(2)))
        .andExpect(jsonPath("$.bookReferenceId", is("2b-2c-2")))
        .andExpect(jsonPath("$.categoryId", is(2)))
        .andExpect(jsonPath("$.available", is(true)));
    checkCount(2, 2);
  }

  @Test
  @Order(4)
  @DisplayName("delete, delete: delete all inventory with book id which has one copy loaned, expected 400")
  void testDeleteWithBookIdFailsAsInventoryLoaned() throws Exception {
    mockMvc
        .perform(
            delete(PATH + "/{bookId}", 2)
                .header("lms-key", apiKey))
        .andExpect(status().isNotFound());
  }

  @Test
  @Order(5)
  @DisplayName("delete, delete: delete all inventory with reference id which is loaned, expected 400")
  void testDeleteWithReferenceIdFailsAsInventoryLoaned() throws Exception {
    mockMvc
        .perform(
            delete(PATH + "/{bookId}", 2)
                .queryParam("bookReferenceId", "2b-2c-2")
                .header("lms-key", apiKey))
        .andExpect(status().isNotFound());
  }

  @Test
  @Order(6)
  @DisplayName("post, orderIfAvailable: no inventory available for book id, expected 404")
  void testOrderIfAvailableNoBookAvailable() throws Exception {
    mockMvc
        .perform(
            post(PATH + "/{bookId}/order", 3)
                .header("lms-key", apiKey)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Order(7)
  @DisplayName("post, returnBook: return book with valid ordered book reference id, expected 200")
  void testReturnBook() throws Exception {
    mockMvc
        .perform(
            post(PATH + "/{bookId}/return", 2)
                .queryParam("bookReferenceId", "2b-2c-2")
                .header("lms-key", apiKey)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(true)));
    checkCount(2, 3);
  }

  @Test
  @Order(8)
  @DisplayName("post, save: add new inventory, expected 201")
  void testSave() throws Exception {
    InventoryDTO mockInventory =
        InventoryDTO.builder()
            .bookId(4L)
            .isbn("979-199-640-416-3")
            .categoryId(1)
            .available(true)
            .build();
    mockMvc
        .perform(
            post(PATH)
                .content(objectMapper.writeValueAsString(mockInventory))
                .header("lms-key", apiKey)
                .header("count", 2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$", hasSize(2)));
    checkCount(4, 2);
  }

  @Test
  @Order(9)
  @DisplayName("delete, delete: delete inventory, expected 204")
  void testDelete() throws Exception {
    mockMvc
        .perform(delete(PATH + "/{bookId}", 4).header("lms-key", apiKey))
        .andExpect(status().isNoContent());
    checkCount(4, 0);
  }

  @Test
  @Order(10)
  @DisplayName("delete, delete: delete inventory with book reference, expected 204")
  void testDeleteWithReferenceId() throws Exception {
    mockMvc
        .perform(
            delete(PATH + "/{bookId}", 2)
                .queryParam("bookReferenceId", "2b-2c-2")
                .header("lms-key", apiKey))
        .andExpect(status().isNoContent());
    checkCount(2, 2);
  }

  private void checkCount(int bookId, int expectedCount) throws Exception {
    mockMvc
        .perform(get(PATH + "/{bookId}/count", bookId).header("lms-key", apiKey))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(expectedCount)));
  }
}
