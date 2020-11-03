package com.dhitha.lms.clientbackend.client;

import com.dhitha.lms.clientbackend.dto.AuthRequestDTO;
import com.dhitha.lms.clientbackend.dto.AuthResponseDTO;
import com.dhitha.lms.clientbackend.dto.UserDTO;
import feign.FeignException.FeignClientException;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Client to connect to auth service
 *
 * @author Dhiraj
 */
@FeignClient(name = "lms-auth-service", path = "/api/auth/v1")
public interface AuthClient {
  @PostMapping(
      value = "/authenticate",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  AuthResponseDTO authenticateUser(@RequestBody @Valid AuthRequestDTO authDTO)
      throws FeignClientException;

  @PostMapping(
      value = "/token/verify",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  UserDTO verifyToken(@NotEmpty @RequestBody Map<String, String> tokenMap) throws FeignClientException;
}
