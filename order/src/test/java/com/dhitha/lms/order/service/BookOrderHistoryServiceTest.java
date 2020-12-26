package com.dhitha.lms.order.service;

import com.dhitha.lms.order.entity.BookOrderHistory;
import com.dhitha.lms.order.repository.BookOrderHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

/**
 * Unit Tests for {@link BookOrderHistoryService }
 * @author Dhiraj
 */
@ExtendWith(MockitoExtension.class)
class BookOrderHistoryServiceTest {

  @Mock
  private BookOrderHistoryRepository repositoryMock;
  private BookOrderHistoryService subject;

  @BeforeEach
  void init(){
    subject = new BookOrderHistoryServiceImpl(repositoryMock, new ModelMapper());
  }


}
