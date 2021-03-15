package com.dhitha.lms.clientbackend.controller;

import com.dhitha.lms.clientbackend.client.OrderClient;
import com.dhitha.lms.clientbackend.dto.BookOrderDTO;
import com.dhitha.lms.clientbackend.dto.BookOrderHistoryDTO;
import com.dhitha.lms.clientbackend.dto.UserDTO;
import com.dhitha.lms.clientbackend.util.OrderUtil;
import java.net.URI;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Controller for Orders
 *
 * @author Dhiraj
 */
@RestController
@RequestMapping("/orders")
@PreAuthorize("hasAuthority('USER')")
@RequiredArgsConstructor
public class ClientOrderController {

  private final OrderClient client;

  private final OrderUtil orderUtil;


  @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllByUser(Authentication authentication) {
    UserDTO userDTO = (UserDTO)authentication.getPrincipal();
    List<BookOrderDTO> userOrders = client.findAllByUser(userDTO.getId());
    return ResponseEntity.ok(userOrders);
  }

  @GetMapping(value = "/me/history", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderHistoryDTO>> findAllHistoryOfUser(Authentication authentication) {
    UserDTO userDTO = (UserDTO)authentication.getPrincipal();
    List<BookOrderHistoryDTO> userOrders = client.findAllHistoryOfUser(userDTO.getId());
    return ResponseEntity.ok(userOrders);
  }

  @GetMapping(value = "/me/overdue/return", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllReturnOverdueOfUser(Authentication authentication) {
    UserDTO userDTO = (UserDTO)authentication.getPrincipal();
    List<BookOrderDTO> allReturnOverdue = client.findAllReturnOverdue(userDTO.getId());
    return ResponseEntity.ok(allReturnOverdue);
  }

  @GetMapping(
      value = "/me/overdue/collect",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllCollectionOverdueOfUser(
      Authentication authentication) {
    UserDTO userDTO = (UserDTO)authentication.getPrincipal();
    List<BookOrderDTO> allCollectionOverdue = client.findAllCollectionOverdue(userDTO.getId());
    return ResponseEntity.ok(allCollectionOverdue);
  }

  @PostMapping( value = "/me",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookOrderDTO> orderBook(@RequestBody BookOrderDTO orderDTO, Authentication authentication) {
    UserDTO userDTO = (UserDTO)authentication.getPrincipal();
    orderDTO.setUserId(userDTO.getId());
    BookOrderDTO savedOrder = client.orderBook(orderDTO);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedOrder.getId())
            .toUri();
    return ResponseEntity.created(uri).body(savedOrder);
  }


}
