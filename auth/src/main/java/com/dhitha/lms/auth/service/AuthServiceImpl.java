package com.dhitha.lms.auth.service;

import com.dhitha.lms.auth.client.UserClient;
import com.dhitha.lms.auth.dto.AuthRequestDTO;
import com.dhitha.lms.auth.dto.AuthResponseDTO;
import com.dhitha.lms.auth.dto.UserDTO;
import com.dhitha.lms.auth.error.GenericException;
import feign.FeignException;
import feign.FeignException.FeignClientException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Implementation for {@link AuthService}
 * @author Dhiraj */
@Service
@RequiredArgsConstructor
@Log4j2
public class AuthServiceImpl implements AuthService {

  private final UserClient userClient;

  private final TokenService tokenService;

  @Override
  public AuthResponseDTO authenticate(AuthRequestDTO authDTO) throws GenericException {
    try {
      UserDTO userDTO = userClient.getByCredentials(authDTO);
      String idToken = tokenService.generateIdToken(userDTO);
      StringTokenizer stringTokenizer = new StringTokenizer(idToken, "\\.");
      return AuthResponseDTO.builder()
          .header(stringTokenizer.nextToken())
          .payload(stringTokenizer.nextToken())
          .signature(stringTokenizer.nextToken())
          .build();
    } catch (FeignException.NotFound e) {
      throw new GenericException("Invalid Username / Password", e.status());
    } catch (NoSuchElementException | FeignClientException e) {
      log.error("authenticate:(AuthRequestDTO) ->  Error connecting to User Service: ", e);
      throw new GenericException();
    }
  }

  @Override
  public UserDTO verifyToken(String token) throws GenericException {
     return tokenService.verifyToken(token);
  }
}
