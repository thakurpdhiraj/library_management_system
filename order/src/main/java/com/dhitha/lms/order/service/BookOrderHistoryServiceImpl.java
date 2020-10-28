package com.dhitha.lms.order.service;

import com.dhitha.lms.order.dto.BookOrderHistoryDTO;
import com.dhitha.lms.order.entity.BookOrder;
import com.dhitha.lms.order.entity.BookOrderHistory;
import com.dhitha.lms.order.repository.BookOrderHistoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * Implementation for {@link BookOrderHistoryService}
 *
 * @author Dhiraj
 */
@Service
@RequiredArgsConstructor
public class BookOrderHistoryServiceImpl implements BookOrderHistoryService {

  private final BookOrderHistoryRepository bookOrderHistoryRepository;
  private final ModelMapper modelMapper;

  @Override
  public void save(BookOrder bookOrder) {
    bookOrderHistoryRepository.saveAndFlush(mapToHistory(bookOrder));
  }

  @Override
  public List<BookOrderHistoryDTO> findAllForUser(Long userId) {
    return bookOrderHistoryRepository.findByUserId(userId).stream()
        .map(this::mapToHistoryDTO)
        .collect(Collectors.toUnmodifiableList());
  }

  private BookOrderHistory mapToHistory(BookOrder bookOrder) {
    modelMapper
        .typeMap(BookOrder.class, BookOrderHistory.class)
        .addMappings(mapper -> mapper.map(BookOrder::getId, BookOrderHistory::setOrderId));
    return modelMapper.map(bookOrder, BookOrderHistory.class);
  }

  private BookOrderHistoryDTO mapToHistoryDTO(BookOrderHistory bookOrderHistory) {
    return modelMapper.map(bookOrderHistory, BookOrderHistoryDTO.class);
  }
}
