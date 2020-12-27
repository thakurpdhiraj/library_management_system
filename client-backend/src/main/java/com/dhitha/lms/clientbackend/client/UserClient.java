package com.dhitha.lms.clientbackend.client;

import com.dhitha.lms.clientbackend.dto.UserDTO;
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
 * Client to connect to LMS-USER-SERVICE registered in the Eureka server with name {@literal
 * "lms-user-service"}
 *
 * @author Dhiraj
 */
@FeignClient(name = "lms-user-service", path = "/api/users/v1")
public interface UserClient {

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  List<UserDTO> getAll();

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  UserDTO get(@PathVariable Long id);

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  UserDTO save(@RequestBody UserDTO userDTO);

  @PutMapping(
      value = "/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  UserDTO update(@PathVariable Long id, @RequestBody UserDTO user);

  @DeleteMapping("/{id}")
  void delete(@PathVariable Long id);
}
