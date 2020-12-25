package com.dhitha.lms.order.service;

import com.dhitha.lms.order.client.InventoryClient;
import com.dhitha.lms.order.dto.InventoryDTO;
import com.dhitha.lms.order.error.GenericException;
import feign.FeignException;
import feign.FeignException.FeignClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/** @author Dhiraj */
@Service
@RequiredArgsConstructor
@Log4j2
public class InventoryServiceImpl implements InventoryService {

  private final InventoryClient inventoryClient;

  @Override
  public InventoryDTO orderIfAvailable(Long bookId) throws GenericException {
    try {

      return inventoryClient.orderIfAvailable(bookId);
    } catch (FeignException.NotFound e) {
      throw new GenericException("Out of stock for Book Id: " + bookId, e.status());
    } catch (FeignClientException ex) {
      log.error("Error connecting to Inventory 'order' api ", ex);
      throw new GenericException("Something went wrong", ex.status());
    }
  }

  @Override
  public void returnBook(Long bookId, String bookReferenceId) throws GenericException {
    try {
      if (!inventoryClient.returnBook(bookId, bookReferenceId)) {
        throw new GenericException(
            String.format(
                "No Book Inventory found with id %s and reference id %s", bookId, bookReferenceId),
            HttpStatus.BAD_REQUEST.value());
      }
    } catch (FeignClientException ex) {
      log.error("Error connecting to Inventory 'return' api ", ex);
      throw new GenericException("Something went wrong", ex.status());
    }
  }
}
