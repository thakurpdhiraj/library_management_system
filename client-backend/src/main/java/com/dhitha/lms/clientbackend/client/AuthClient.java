package com.dhitha.lms.clientbackend.client;

import com.dhitha.lms.clientbackend.dto.AuthRequestDTO;
import com.dhitha.lms.clientbackend.dto.AuthResponseDTO;
import feign.FeignException.FeignClientException;
import javax.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Client to connect to LMS-AUTH-SERVICE registered in the Eureka server with name {@literal
 * "lms-auth-service"}
 *
 * @author Dhiraj
 */
@FeignClient(name = "lms-auth-service", path = "/api/auth/v1")
public interface AuthClient {
  @PostMapping(
      value = "/token/authenticate",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  AuthResponseDTO authenticateUser(@RequestBody @Valid AuthRequestDTO authDTO)
      throws FeignClientException;

  @PostMapping(
      value = "/token/verify",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  AuthResponseDTO verifyToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token)
      throws FeignClientException;
}
