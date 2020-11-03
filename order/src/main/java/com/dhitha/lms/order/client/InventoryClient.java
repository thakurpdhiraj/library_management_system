package com.dhitha.lms.order.client;

import com.dhitha.lms.order.dto.InventoryDTO;
import feign.FeignException.FeignClientException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Client to connect to LMS-INVENTORY-SERVICE registered in the Eureka server with name {@literal
 * "lms-inventory-service"}
 *
 * @author Dhiraj
 */
@FeignClient(name = "lms-inventory-service", path = "/api/inventory/v1")
public interface InventoryClient {

  /**
   * Order a book if available
   *
   * @param bookId -
   * @return inventory detail containing
   * @throws FeignClientException in case of an exception status returned from the service
   */
  @PostMapping(
      value = "/books/{bookId}/order",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  InventoryDTO orderIfAvailable(@PathVariable Long bookId) throws FeignClientException;

  /**
   * Return a book in the inventory
   *
   * @param bookId -
   * @param bookReferenceId -
   * @return if returning of book to inventory was successful
   * @throws FeignClientException in case of an exception status returned from the service
   */
  @PostMapping(
      value = "/books/{bookId}/return",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  Boolean returnBook(@PathVariable Long bookId, @RequestParam String bookReferenceId)
      throws FeignClientException;
}
