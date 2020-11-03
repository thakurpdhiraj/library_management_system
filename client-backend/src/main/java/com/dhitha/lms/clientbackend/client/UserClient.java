package com.dhitha.lms.clientbackend.client;

import com.dhitha.lms.clientbackend.dto.UserDTO;
import feign.FeignException.FeignClientException;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Client to connect to user service
 *
 * @author Dhiraj
 */
@FeignClient(name = "lms-user-service", path = "/api/users/v1")
public interface UserClient {

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  List<UserDTO> getAll() throws FeignClientException;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  UserDTO get(@PathVariable Long id) throws FeignClientException;

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  UserDTO save(@RequestBody UserDTO userDTO) throws FeignClientException;

  @PutMapping(
      value = "/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  UserDTO update(@PathVariable Long id, @RequestBody UserDTO user);

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  void delete(@PathVariable Long id) throws FeignClientException;
}
