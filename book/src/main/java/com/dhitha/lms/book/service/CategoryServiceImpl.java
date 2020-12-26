package com.dhitha.lms.book.service;

import com.dhitha.lms.book.dto.CategoryDTO;
import com.dhitha.lms.book.entity.Category;
import com.dhitha.lms.book.error.CategoryNotFoundException;
import com.dhitha.lms.book.error.GenericException;
import com.dhitha.lms.book.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Implementation of {@link CategoryService}
 *
 * @author Dhiraj
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  private final ModelMapper modelMapper;

  @Override
  public CategoryDTO findById(Integer id) throws CategoryNotFoundException {
    return categoryRepository
        .findById(id)
        .map(this::mapToDTO)
        .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: "+id));
  }

  @Override
  public List<CategoryDTO> findAll() {
    return categoryRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
  }

  @Override
  public CategoryDTO save(CategoryDTO categoryDTO) throws GenericException {
    try {
      categoryDTO.setId(null);
      Category category = categoryRepository.saveAndFlush(mapToEntity(categoryDTO));
      return mapToDTO(category);
    } catch (DataIntegrityViolationException e) {
      log.warn("Error: {} : {}", e.getCause(), e.getMessage());
      throw new GenericException(
          "Category with name :'" + categoryDTO.getName() + "' already present", 400);
    }
  }

  @Override
  public CategoryDTO update(CategoryDTO categoryDTO)
      throws CategoryNotFoundException, GenericException {
    CategoryDTO dbCategory = this.findById(categoryDTO.getId());
    if (StringUtils.isEmpty(categoryDTO.getName()) || dbCategory.getName().equals(categoryDTO.getName())) {
      return dbCategory;
    }
    try {
      Category updatedCategory = categoryRepository.saveAndFlush(mapToEntity(categoryDTO));
      return mapToDTO(updatedCategory);
    } catch (DataIntegrityViolationException e) {
      log.warn("Error: {} : {}", e.getCause(), e.getMessage());
      throw new GenericException(
          "Category with name :'" + categoryDTO.getName() + "' already present", 400);
    }
  }

  @Override
  public void delete(Integer id) throws CategoryNotFoundException {
    try{
    categoryRepository.deleteById(id);
    }catch (EmptyResultDataAccessException e){
      throw new CategoryNotFoundException("Category not found with id: "+id);
    }
  }

  private CategoryDTO mapToDTO(Category category) {
    return modelMapper.map(category, CategoryDTO.class);
  }

  private Category mapToEntity(CategoryDTO categoryDTO) {
    return modelMapper.map(categoryDTO, Category.class);
  }
}
