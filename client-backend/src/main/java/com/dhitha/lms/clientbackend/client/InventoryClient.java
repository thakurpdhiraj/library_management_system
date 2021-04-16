package com.dhitha.lms.clientbackend.client;

import com.dhitha.lms.clientbackend.dto.InventoryDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Client to connect to LMS-INVENTORY-SERVICE registered in the Eureka server with name {@literal
 * "lms-inventory-service"}
 *
 * @author Dhiraj
 */
@FeignClient(name = "lms-inventory-service", path = "/api/inventory/v1/books")
public interface InventoryClient {

  @GetMapping(value = "/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
  List<InventoryDTO> getAllInventoryOfBook(@PathVariable Long bookId);

  @GetMapping(value = "/{bookId}/count", produces = MediaType.APPLICATION_JSON_VALUE)
  Long getAvailableCountOfBook(@PathVariable Long bookId);

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  List<InventoryDTO> save(
      @RequestBody InventoryDTO inventoryDTO,
      @RequestHeader(value = "count", required = false) Integer count);

  @DeleteMapping(value = "/{bookId}")
  void delete(@PathVariable Long bookId, @RequestParam(required = false) List<String> bookReferenceId);
}
