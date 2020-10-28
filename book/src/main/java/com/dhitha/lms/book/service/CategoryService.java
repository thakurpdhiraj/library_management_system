package com.dhitha.lms.book.service;

import com.dhitha.lms.book.dto.CategoryDTO;
import com.dhitha.lms.book.error.CategoryNotFoundException;
import com.dhitha.lms.book.error.GenericException;
import java.util.List;

/**
 * Service for {@link CategoryDTO}
 *
 * @author Dhiraj
 */
public interface CategoryService {

  /**
   * Find Category by ID
   *
   * @param id -
   * @return category corresponding to the id
   * @throws CategoryNotFoundException in case no category exists with id
   */
  CategoryDTO findById(Integer id) throws CategoryNotFoundException;

  /**
   * Find all category in LMS
   *
   * @return list of category
   */
  List<CategoryDTO> findAll();

  /**
   * Add a new Category with unique name
   *
   * @param categoryDTO - new category to persist
   * @return added category with new generated id
   * @throws GenericException if category with same name present
   */
  CategoryDTO save(CategoryDTO categoryDTO) throws GenericException;

  /**
   * Update an existing category
   *
   * @param categoryDTO category to update, id must not be null
   * @return updated category, null values are ignored
   * @throws CategoryNotFoundException in case no category exists with id
   * @throws GenericException if name of category is changed and category with same name exists
   */
  CategoryDTO update(CategoryDTO categoryDTO) throws CategoryNotFoundException, GenericException;

  /**
   * Delete a category
   *
   * @param id -
   * @throws CategoryNotFoundException in case no category exists with id
   */
  void delete(Integer id) throws CategoryNotFoundException;
}
