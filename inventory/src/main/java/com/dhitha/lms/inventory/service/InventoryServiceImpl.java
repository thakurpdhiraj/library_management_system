package com.dhitha.lms.inventory.service;

import com.dhitha.lms.inventory.dto.InventoryDTO;
import com.dhitha.lms.inventory.entity.Inventory;
import com.dhitha.lms.inventory.error.GenericException;
import com.dhitha.lms.inventory.error.InventoryNotFoundException;
import com.dhitha.lms.inventory.repository.InventoryRepository;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Implementation for {@link InventoryService}
 *
 * @author Dhiraj
 */
@Service
@Slf4j
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
  public long getAvailableCount(Long bookId) {
    return inventoryRepository.countByIdBookIdAndAvailable(bookId, true);
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public List<InventoryDTO> add(InventoryDTO inventoryDTO, Integer count) throws GenericException {
    Assert.notNull(inventoryDTO, "Inventory cannot be null");
    count = count != null && count > 0 ? count : 1;
    Set<Inventory> set = new HashSet<>(count);
    SecureRandom secureRandom = new SecureRandom();
    while (count-- > 0) {
      Inventory inventory = this.mapToEntity(inventoryDTO);
      inventory.getId().setBookReferenceId(String.valueOf(secureRandom.nextInt(Integer.MAX_VALUE)));
      inventory.setAvailable(true);
      set.add(inventory);
    }
    try {
      inventoryRepository.saveAll(set);
    } catch (Exception e) {
      throw new GenericException(
          "Error while saving inventory", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
    return set.stream().map(this::mapToDTO).collect(Collectors.toList());
  }

  @Override
  public void delete(Long bookId) throws InventoryNotFoundException {
    List<Inventory> bookInventory = inventoryRepository.findByIdBookId(bookId);
    if (bookInventory.isEmpty()) {
      return;
    }
    long count = bookInventory.stream().filter(inventory -> !inventory.getAvailable()).count();
    if (count > 0) {
      throw new InventoryNotFoundException(
          "Some books of book id " + bookId + " are loaned and cannot be deleted ");
    }
    inventoryRepository.deleteByIdBookId(bookId);
  }

  @Override
  @Transactional(rollbackOn = InventoryNotFoundException.class)
  public void delete(Long bookId, List<String> bookReferenceIdList) throws InventoryNotFoundException {
    for(String bookReferenceId: bookReferenceIdList) {
      Optional<Inventory> inventory =
          inventoryRepository.findByIdBookIdAndIdBookReferenceId(bookId, bookReferenceId);
      if (inventory.isEmpty()) {
        throw new InventoryNotFoundException(
            String.format("No Book found with id %s and reference id %s", bookId, bookReferenceId));
      }
      if (!inventory.get().getAvailable()) {
        throw new InventoryNotFoundException(
            String.format(
                "Book with id %s and reference id %s is loaned and cannot be deleted",
                bookId, bookReferenceId));
      }
      inventoryRepository.deleteByIdBookIdAndIdBookReferenceId(bookId, bookReferenceId);
    }
  }

  private InventoryDTO mapToDTO(Inventory inventory) {
    return modelMapper.map(inventory, InventoryDTO.class);
  }

  private Inventory mapToEntity(InventoryDTO inventoryDTO) {
    return modelMapper.map(inventoryDTO, Inventory.class);
  }
}
