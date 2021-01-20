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


  @GetMapping(value = "/users/me", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllByUser(Authentication authentication) {
    UserDTO userDTO = (UserDTO)authentication.getPrincipal();
    List<BookOrderDTO> userOrders = client.findAllByUser(userDTO.getId());
    orderUtil.setBookNameForOrderList(userOrders);
    return ResponseEntity.ok(userOrders);
  }

  @GetMapping(value = "/users/me/history", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderHistoryDTO>> findAllHistoryOfUser(Authentication authentication) {
    UserDTO userDTO = (UserDTO)authentication.getPrincipal();
    Map<Long, String> bookNames = orderUtil.getAllBookNames();
    List<BookOrderHistoryDTO> userOrders = client.findAllHistoryOfUser(userDTO.getId());
    userOrders.forEach(bookOrderDTO -> bookOrderDTO.setBookName(bookNames.get(bookOrderDTO.getBookId())));
    return ResponseEntity.ok(userOrders);
  }

  @GetMapping(value = "/users/me/overdue/return", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllReturnOverdueOfUser(Authentication authentication) {
    UserDTO userDTO = (UserDTO)authentication.getPrincipal();
    List<BookOrderDTO> allReturnOverdue = client.findAllReturnOverdue(userDTO.getId());
    orderUtil.setBookNameForOrderList(allReturnOverdue);
    return ResponseEntity.ok(allReturnOverdue);
  }

  @GetMapping(
      value = "/users/me/overdue/collect",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BookOrderDTO>> findAllCollectionOverdueOfUser(
      Authentication authentication) {
    UserDTO userDTO = (UserDTO)authentication.getPrincipal();
    List<BookOrderDTO> allCollectionOverdue = client.findAllCollectionOverdue(userDTO.getId());
    orderUtil.setBookNameForOrderList(allCollectionOverdue);
    return ResponseEntity.ok(allCollectionOverdue);
  }

  @PostMapping(
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
    Map<Long, String> bookNames = orderUtil.getAllBookNames();
    savedOrder.setBookName(bookNames.get(savedOrder.getBookId()));
    return ResponseEntity.created(uri).body(savedOrder);
  }


}
