package com.dhitha.lms.order.service;

import com.dhitha.lms.order.dto.BookOrderDTO;
import com.dhitha.lms.order.dto.InventoryDTO;
import com.dhitha.lms.order.entity.BookOrder;
import com.dhitha.lms.order.error.GenericException;
import com.dhitha.lms.order.error.OrderNotFoundException;
import com.dhitha.lms.order.repository.BookOrderRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Implementation for {@link BookOrderService}
 *
 * @author Dhiraj
 */
@Service
@RequiredArgsConstructor
public class BookOrderServiceImpl implements BookOrderService {

  private final BookOrderRepository orderRepository;
  private final BookOrderHistoryService bookOrderHistoryService;
  private final ModelMapper modelMapper;
  private final InventoryService inventoryService;

  private static final int LATE_FEES_PER_DAY = 100;

  @Override
  public BookOrderDTO findById(Long id) throws OrderNotFoundException {
    return orderRepository
        .findById(id)
        .map(this::mapToDTO)
        .orElseThrow(() -> new OrderNotFoundException("Order Not found with id " + id));
  }

  @Override
  public List<BookOrderDTO> findAllByUser(Long userId) {
    return orderRepository.findByUserId(userId).stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<BookOrderDTO> findAllByBook(Long bookId) {
    return orderRepository.findByBookId(bookId).stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<BookOrderDTO> findAllReturnOverdue() {
    return orderRepository.findByReturnByBefore(LocalDateTime.now()).stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<BookOrderDTO> findAllReturnOverdueForUser(Long userId) {
    return orderRepository.findByReturnByBeforeAndUserId(LocalDateTime.now(), userId).stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<BookOrderDTO> findAllCollectionOverdue() {
    return orderRepository.findByCollectByBefore(LocalDateTime.now()).stream()
        .filter(order -> order.getCollectedAt() == null)
        .map(this::mapToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<BookOrderDTO> findAllCollectionOverdueForUser(Long userId) {
    return orderRepository.findByCollectByBeforeAndUserId(LocalDateTime.now(), userId).stream()
        .filter(order -> order.getCollectedAt() == null)
        .map(this::mapToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public BookOrderDTO orderBook(BookOrderDTO orderDTO) throws GenericException {
    boolean orderExists =
        orderRepository.existsByUserIdAndBookId(orderDTO.getUserId(), orderDTO.getBookId());
    if (!orderExists) {
      //TODO: Check if user is valid
      InventoryDTO inventoryDTO = inventoryService.orderIfAvailable(orderDTO.getBookId());
      orderDTO.setBookReferenceId(inventoryDTO.getBookReferenceId());
      LocalDateTime now = LocalDateTime.now();
      orderDTO.setCollectBy(now.plusDays(3));
      orderDTO.setReturnBy(now.plusMonths(1));
      orderDTO.setId(null);
      BookOrder order = orderRepository.saveAndFlush(mapToEntity(orderDTO));
      return mapToDTO(order);
    } else {
      throw new GenericException(
          String.format(
              "User with id %s already has order with book %s",
              orderDTO.getUserId(), orderDTO.getBookId()),
          HttpStatus.FORBIDDEN.value());
    }
  }

  @Override
  public BookOrderDTO collectBook(Long id) throws OrderNotFoundException {
    // TODO: check for collection overdue
    BookOrder order =
        orderRepository
            .findById(id)
            .filter(bookOrder -> bookOrder.getCollectedAt() == null)
            .orElseThrow(
                () ->
                    new OrderNotFoundException(
                        "Order Already Collected or Not found with id " + id));
    order.setCollectedAt(LocalDateTime.now());
    return mapToDTO(orderRepository.saveAndFlush(order));
  }

  @Override
  public BookOrderDTO returnBook(Long id) throws OrderNotFoundException, GenericException {
    BookOrder order =
        orderRepository
            .findById(id)
            .orElseThrow(() -> new OrderNotFoundException("Order Not found with id " + id));
    LocalDateTime now = LocalDateTime.now();
    long difference = Duration.between(order.getReturnBy(),now).toDays();
    long lateFees = 0;
    if(difference > 0) {
      lateFees = LATE_FEES_PER_DAY * difference;
    }
    inventoryService.returnBook(order.getBookId(), order.getBookReferenceId());
    order.setLateFees(lateFees);
    order.setReturnedAt(LocalDateTime.now());
    this.delete(id);
    bookOrderHistoryService.save(order);
    return mapToDTO(order);
  }

  @Override
  public void delete(Long id) throws OrderNotFoundException {
    try {
      orderRepository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new OrderNotFoundException("Order Not found with id " + id);
    }
  }

  private BookOrderDTO mapToDTO(BookOrder order) {
    return modelMapper.map(order, BookOrderDTO.class);
  }

  private BookOrder mapToEntity(BookOrderDTO orderDTO) {
    return modelMapper.map(orderDTO, BookOrder.class);
  }
}
