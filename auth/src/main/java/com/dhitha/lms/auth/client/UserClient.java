package com.dhitha.lms.auth.client;

import com.dhitha.lms.auth.dto.AuthRequestDTO;
import com.dhitha.lms.auth.dto.UserDTO;
import feign.FeignException.FeignClientException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Client to connect to LMS-USER-SERVICE registered in the Eureka server with name {@literal
 * "lms-user-service"}
 *
 * @author Dhiraj
 */
@FeignClient(name = "lms-user-service", path = "/api/users/v1")
public interface UserClient {
  @PostMapping(
      value = "/validate",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  UserDTO getByCredentials(@RequestBody AuthRequestDTO user) throws FeignClientException;
}
