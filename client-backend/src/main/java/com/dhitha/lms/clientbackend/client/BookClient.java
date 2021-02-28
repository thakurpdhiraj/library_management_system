package com.dhitha.lms.clientbackend.client;

import com.dhitha.lms.clientbackend.dto.BookDTO;
import com.dhitha.lms.clientbackend.dto.CategoryDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Client to connect to LMS-BOOK-SERVICE registered in the Eureka server with name {@literal
 * "lms-book-service"}
 *
 * @author Dhiraj
 */
@FeignClient(name = "lms-book-service", path = "/api")
public interface BookClient {

  @GetMapping(value = "/books/v1", produces = MediaType.APPLICATION_JSON_VALUE)
  List<BookDTO> getAllBooks(@SpringQueryMap BookDTO bookDTO,
      @RequestParam(value = "categoryId", required = false) Integer categoryId);

  @GetMapping(value = "/books/v1/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  BookDTO getById(@PathVariable Long id);

  @PostMapping(value = "/books/v1",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  BookDTO save(@RequestBody BookDTO bookDTO);

  @PutMapping(
      value = "/books/v1/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  BookDTO update(@PathVariable Long id, @RequestBody BookDTO bookDTO);

  @DeleteMapping(value = "/books/v1/{id}")
  void delete(@PathVariable Long id);

  /*************************Category**************************/

  @GetMapping(value = "/categories/v1",produces = MediaType.APPLICATION_JSON_VALUE)
  List<CategoryDTO> findAllCategory();

  @GetMapping(value = "/categories/v1/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  CategoryDTO findCategoryById(@PathVariable Integer id);

  @PostMapping(value = "/categories/v1",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  CategoryDTO saveCategory(@RequestBody CategoryDTO categoryDTO);

  @PutMapping(
      value = "/categories/v1/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  CategoryDTO updateCategory(@PathVariable Integer id, @RequestBody CategoryDTO categoryDTO);

  @DeleteMapping(value = "/categories/v1/{id}")
  void deleteCategory(@PathVariable Integer id);
}
