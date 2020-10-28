package com.dhitha.lms.inventory.service;

import com.dhitha.lms.inventory.dto.InventoryDTO;
import com.dhitha.lms.inventory.entity.Inventory;
import com.dhitha.lms.inventory.entity.InventoryId;
import com.dhitha.lms.inventory.error.GenericException;
import com.dhitha.lms.inventory.error.InventoryNotFoundException;
import com.dhitha.lms.inventory.repository.InventoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Implementation for {@link InventoryService}
 *
 * @author Dhiraj
 */
@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

  private final InventoryRepository inventoryRepository;
  private final ModelMapper modelMapper;

  @Override
  public List<InventoryDTO> findAllBook(Long bookId) {
    return inventoryRepository.findByIdBookId(bookId).stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public InventoryDTO orderBookIfAvailable(Long bookId) throws InventoryNotFoundException {
    InventoryDTO inventoryDTO =
        inventoryRepository.findByIdBookId(bookId).stream()
            .filter(Inventory::getAvailable)
            .findFirst()
            .map(this::mapToDTO)
            .orElseThrow(
                () ->
                    new InventoryNotFoundException(
                        "No inventory available for book id: " + bookId));
    inventoryRepository.updateAvailability(
        inventoryDTO.getBookId(), inventoryDTO.getBookReferenceId(), false);
    return inventoryDTO;
  }

  @Override
  public boolean returnBook(Long bookId, String bookReferenceId) {
    return inventoryRepository.updateAvailability(bookId, bookReferenceId, true) > 0;
  }

  @Override
  public long availableCount(Long bookId) {
    return inventoryRepository.countByIdBookIdAndAvailable(bookId,true);
  }

  @Override
  public void add(InventoryDTO inventoryDTO) throws GenericException {
    try {
      inventoryRepository.saveAndFlush(mapToEntity(inventoryDTO));
    } catch (DataIntegrityViolationException e) {
      throw new GenericException(
          String.format(
              "Book with id %s already has reference id %s",
              inventoryDTO.getBookId(), inventoryDTO.getBookReferenceId()),
          HttpStatus.BAD_REQUEST.value());
    }
  }

  @Override
  public void delete(Long bookId) throws InventoryNotFoundException {
    try {
      inventoryRepository.deleteByIdBookId(bookId);
    } catch (EmptyResultDataAccessException e) {
      throw new InventoryNotFoundException("No inventory found for book id: " + bookId);
    }
  }

  @Override
  public void delete(Long bookId, String bookReferenceId) throws InventoryNotFoundException {
    try {
      inventoryRepository.deleteById(new InventoryId(bookId, bookReferenceId));
    } catch (EmptyResultDataAccessException e) {
      throw new InventoryNotFoundException(
          String.format("No Book found with id %s and reference id %s", bookId, bookReferenceId));
    }
  }

  private InventoryDTO mapToDTO(Inventory inventory) {
    return modelMapper.map(inventory, InventoryDTO.class);
  }

  private Inventory mapToEntity(InventoryDTO inventoryDTO) {
    return modelMapper.map(inventoryDTO, Inventory.class);
  }
}
