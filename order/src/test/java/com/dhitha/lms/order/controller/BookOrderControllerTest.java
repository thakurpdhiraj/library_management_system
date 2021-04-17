package com.dhitha.lms.order.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dhitha.lms.order.dto.BookOrderDTO;
import com.dhitha.lms.order.dto.InventoryDTO;
import com.dhitha.lms.order.error.GenericException;
import com.dhitha.lms.order.service.InventoryService;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for {@link BookOrderController}. Depends on src/test/resources/data-test.sql
 *
 * @author Dhiraj
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class BookOrderControllerTest {

  @MockBean private InventoryService inventoryService;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private MockMvc mockMvc;

  @Value("${lms.client.key}")
  private String apiKey;

  @Test
  @Order(0)
  @DisplayName("get, findById: Test api without api key authentication, expected 403")
  void testAuthenticationApiKey() throws Exception {
    mockMvc.perform(get("/v1/{id}", 1)).andExpect(status().isForbidden());
  }

  @Test
  @Order(1)
  @DisplayName("get, findById: Find order by id, expected 200")
  void testFindById() throws Exception {
    mockMvc
        .perform(get("/v1/{id}", 1).header("lms-key", apiKey))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.userId", is(1)))
        .andExpect(jsonPath("$.bookId", is(2)))
        .andExpect(jsonPath("$.bookReferenceId", is("2b-1c-abc")));
  }

  @Test
  @Order(2)
  @DisplayName("get, findAllByUser: Find all orders of user, expected 200")
  void testFindAllByUser() throws Exception {
    mockMvc
        .perform(get("/v1/users/{id}", 1).header("lms-key", apiKey))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(4)));
  }

  @Test
  @Order(3)
  @DisplayName("get, findAllByBook: Find all orders for a book, expected 200")
  void testFindAllByBook() throws Exception {
    mockMvc
        .perform(get("/v1/books/{id}", 2).header("lms-key", apiKey))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  @Order(4)
  @DisplayName(
      "get, findAllReturnOverdue: Find all orders that are overdue for return, expected 200")
  void testFindAllReturnOverdue() throws Exception {
    mockMvc
        .perform(get("/v1/overdue/return").header("lms-key", apiKey))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  @Order(5)
  @DisplayName(
      "get, findAllReturnOverdue: Find all orders that are overdue for return for a user, expected 200")
  void testFindAllReturnOverdueForUser() throws Exception {
    mockMvc
        .perform(get("/v1/overdue/return").header("lms-key", apiKey).queryParam("userId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
  }

  @Test
  @Order(6)
  @DisplayName("get, findAllByBook: Find all orders that are overdue for collection, expected 200")
  void testFindAllCollectionOverdue() throws Exception {
    mockMvc
        .perform(get("/v1/overdue/return").header("lms-key", apiKey))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  @Order(7)
  @DisplayName(
      "get, findAllCollectionOverdue: Find all orders that are overdue for collection for a user, expected 200")
  void testFindAllCollectionOverdueForUser() throws Exception {
    mockMvc
        .perform(get("/v1/overdue/return").header("lms-key", apiKey).queryParam("userId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
  }

  @Test
  @Order(8)
  @DisplayName("post, orderBook: Successfully order a book, expected 201")
  void testOrderBook() throws Exception {
    InventoryDTO mockInventory =
        InventoryDTO.builder().bookId(5L).bookReferenceId("5b-1c-1").categoryId(1).build();
    when(inventoryService.orderIfAvailable(5L)).thenReturn(mockInventory);
    BookOrderDTO mockOrder =
        BookOrderDTO.builder().userId(1L).bookId(5L).bookIsbn("isbn").bookName("name").build();
    mockMvc
        .perform(
            post("/v1")
                .header("lms-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockOrder)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(jsonPath("$.id", is(7)))
        .andExpect(jsonPath("$.collectedAt", nullValue()))
        .andExpect(jsonPath("$.returnedAt", nullValue()));
  }

  @Test
  @Order(9)
  @DisplayName("post, orderBook: User already has book ordered, expected 403")
  void testOrderBookUserNotAllowedBook() throws Exception {
    BookOrderDTO mockOrder =
        BookOrderDTO.builder().userId(1L).bookIsbn("isbn").bookName("name").bookId(2L).build();
    mockMvc
        .perform(
            post("/v1")
                .header("lms-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockOrder)))
        .andExpect(status().isForbidden());
  }

  @Test
  @Order(10)
  @DisplayName(
      "post, orderBook: Exception in Inventory Service, Inventory Not Available, expected 404")
  void testOrderBookInventoryNotAvailable() throws Exception {
    when(inventoryService.orderIfAvailable(6L)).thenThrow(new GenericException("", 404));
    BookOrderDTO mockOrder =
        BookOrderDTO.builder().userId(1L).bookIsbn("isbn").bookName("name").bookId(6L).build();
    mockMvc
        .perform(
            post("/v1")
                .header("lms-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockOrder)))
        .andExpect(status().isNotFound());
  }

  @Test
  @Order(11)
  @DisplayName("put, collectBook: Successfully collected, expected 200")
  void testCollectBookSuccess() throws Exception {
    mockMvc
        .perform(
            put("/v1/{id}/collect", 2).header("lms-key", apiKey).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.collectedAt", notNullValue()));
  }

  @Test
  @Order(12)
  @DisplayName("put, collectBook: Error while collecting already collected book, expected 404")
  void testCollectBookErrorOnAlreadyCollectedBook() throws Exception {
    mockMvc
        .perform(
            put("/v1/{id}/collect", 4).header("lms-key", apiKey).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  @Order(13)
  @DisplayName("put, collectBook: Error while collecting already collected book, expected 404")
  void testReturnBookSuccess() throws Exception {
    doNothing().when(inventoryService).returnBook(3L, "3b-1c-abc");
    mockMvc
        .perform(put("/v1/{id}/return", 3).header("lms-key", apiKey))
        .andExpect(status().isOk());
    mockMvc.perform(get("/v1/{id}", 3).header("lms-key", apiKey)).andExpect(status().isNotFound());

    mockMvc
        .perform(get("/v1/users/{userId}/history", 1).header("lms-key", apiKey))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
  }
}
