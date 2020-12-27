package com.dhitha.lms.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dhitha.lms.book.dto.CategoryDTO;
import com.dhitha.lms.book.entity.Category;
import com.dhitha.lms.book.error.CategoryNotFoundException;
import com.dhitha.lms.book.repository.CategoryRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * Unit tests for {@link CategoryService}
 *
 * @author Dhiraj
 */
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

  private final ModelMapper modelMapper = new ModelMapper();
  @Mock private CategoryRepository categoryRepositoryMock;
  private CategoryService subject;

  @BeforeEach
  void init() {
    subject = new CategoryServiceImpl(categoryRepositoryMock, modelMapper);
  }

  /* ******************** findById **************************** */
  @Test
  void testFindById() throws Exception {
    when(categoryRepositoryMock.findById(1)).thenReturn(Optional.of(new Category(1, "CAT", null)));
    CategoryDTO result = subject.findById(1);
    verify(categoryRepositoryMock).findById(1);
    assertEquals("CAT", result.getName());
  }

  @Test
  void testFindByIdThrowsCategoryNotFoundException() {
    assertThrows(
        CategoryNotFoundException.class,
        () -> {
          when(categoryRepositoryMock.findById(1)).thenReturn(Optional.empty());
          subject.findById(1);
        });
    verify(categoryRepositoryMock).findById(1);
  }

  /* ******************** findAll **************************** */
  @Test
  void testFindAll() {
    when(categoryRepositoryMock.findAll())
        .thenReturn(Collections.singletonList(new Category(1, "CAT", null)));
    List<CategoryDTO> result = subject.findAll();
    assertThat(result).hasSize(1).contains(new CategoryDTO(1, "CAT"));
  }

  /* ******************** save **************************** */
  @Test
  void testSave() throws Exception {
    when(categoryRepositoryMock.saveAndFlush(any(Category.class)))
        .thenReturn(new Category(1, "CAT", null));
    CategoryDTO result = subject.save(new CategoryDTO(null, "CAT"));
    verify(categoryRepositoryMock).saveAndFlush(any(Category.class));
    assertEquals(1, result.getId());
  }

  /* ******************** update **************************** */

  @Test
  void testUpdate() throws Exception {
    Category mock = new Category(1, "DOG", null);
    when(categoryRepositoryMock.findById(1)).thenReturn(Optional.of(new Category(1, "CAT", null)));
    when(categoryRepositoryMock.saveAndFlush(mock)).thenReturn(mock);
    CategoryDTO result = subject.update(new CategoryDTO(1, "DOG"));
    assertEquals("DOG", result.getName());
    verify(categoryRepositoryMock).findById(1);
    verify(categoryRepositoryMock).saveAndFlush(any(Category.class));
  }

  @Test
  void testUpdateWithSameName() throws Exception {
    when(categoryRepositoryMock.findById(1)).thenReturn(Optional.of(new Category(1, "CAT", null)));
    CategoryDTO result = subject.update(new CategoryDTO(1, "CAT"));
    assertEquals("CAT", result.getName());
    verify(categoryRepositoryMock).findById(1);
    verify(categoryRepositoryMock, never()).saveAndFlush(any(Category.class));
  }

  @Test
  void testUpdateOnNullNameReturnsPreviousStoredCategory() throws Exception {
    when(categoryRepositoryMock.findById(1)).thenReturn(Optional.of(new Category(1, "CAT", null)));
    CategoryDTO result = subject.update(new CategoryDTO(1, null));
    assertEquals("CAT", result.getName());
    verify(categoryRepositoryMock).findById(1);
    verify(categoryRepositoryMock, never()).saveAndFlush(any(Category.class));
  }

  @Test
  void testUpdateThrowsCategoryNotFoundException() {
    assertThrows(
        CategoryNotFoundException.class,
        () -> {
          when(categoryRepositoryMock.findById(1)).thenReturn(Optional.empty());
          subject.update(new CategoryDTO(1, "CAT"));
        });
    verify(categoryRepositoryMock).findById(1);
    verify(categoryRepositoryMock, never()).saveAndFlush(any(Category.class));
  }
  /* ******************** delete **************************** */

  @Test
  void testDelete() throws Exception {
    doNothing().when(categoryRepositoryMock).deleteById(1);
    subject.delete(1);
    verify(categoryRepositoryMock).deleteById(1);
  }

  @Test
  void testDeleteThrowsCategoryNotFoundException() {
    assertThrows(
        CategoryNotFoundException.class,
        () -> {
         doThrow(EmptyResultDataAccessException.class).when(categoryRepositoryMock).deleteById(20);
          subject.delete(20);
        });
    verify(categoryRepositoryMock).deleteById(20);
  }
}
