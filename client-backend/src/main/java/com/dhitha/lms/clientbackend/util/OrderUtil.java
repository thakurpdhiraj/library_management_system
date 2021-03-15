package com.dhitha.lms.clientbackend.util;

import com.dhitha.lms.clientbackend.client.BookClient;
import com.dhitha.lms.clientbackend.dto.BookDTO;
import com.dhitha.lms.clientbackend.dto.BookOrderDTO;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Dhiraj
 */
@Component
@RequiredArgsConstructor
public class OrderUtil {
  private final BookClient bookClient;

//  public void setBookNameForOrderList(List<BookOrderDTO> orders){
//    Map<Long, String> bookNames = getAllBookNames();
//    orders.forEach(bookOrderDTO -> bookOrderDTO.setBookName(bookNames.get(bookOrderDTO.getBookId())));
//  }
//
//  public Map<Long, String> getAllBookNames(){
//    List<BookDTO> allBooks = bookClient.getAllBooks(null, null);
//    return allBooks.stream()
//        .collect(Collectors.toMap(BookDTO::getId, BookDTO::getName));
//  }
}
