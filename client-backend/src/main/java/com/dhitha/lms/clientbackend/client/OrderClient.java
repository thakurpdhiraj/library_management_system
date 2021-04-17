package com.dhitha.lms.clientbackend.client;

import com.dhitha.lms.clientbackend.dto.BookOrderDTO;
import com.dhitha.lms.clientbackend.dto.BookOrderHistoryDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Client to connect to LMS-ORDER-SERVICE registered in the Eureka server with name {@literal
 * "lms-order-service"}
 *
 * @author Dhiraj
 */
@FeignClient(name = "lms-order-service", path = "/api/orders/v1/")
public interface OrderClient {

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  BookOrderDTO findById(@PathVariable Long id);

  @GetMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  List<BookOrderDTO> findAllByUser(@PathVariable Long userId);

  @GetMapping(value = "/users/{userId}/history", produces = MediaType.APPLICATION_JSON_VALUE)
  List<BookOrderHistoryDTO> findAllHistoryOfUser(@PathVariable Long userId);

  @GetMapping(value = "/books/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
  List<BookOrderDTO> findAllByBook(@PathVariable Long bookId);

  @GetMapping(value = "/overdue/return", produces = MediaType.APPLICATION_JSON_VALUE)
  List<BookOrderDTO> findAllReturnOverdue(
      @RequestParam(value = "userId", required = false) Long userId);

  @GetMapping(value = "/overdue/collect", produces = MediaType.APPLICATION_JSON_VALUE)
  List<BookOrderDTO> findAllCollectionOverdue(
      @RequestParam(value = "userId", required = false) Long userId);

  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  BookOrderDTO orderBook(@RequestBody BookOrderDTO orderDTO);

  @PutMapping(value = "/{id}/collect")
  BookOrderDTO collectBook(@PathVariable Long id);

  @PutMapping(value = "/{id}/return")
  BookOrderDTO returnBook(@PathVariable Long id);
}
