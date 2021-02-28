package com.dhitha.lms.auth.service;

import com.dhitha.lms.auth.client.UserClient;
import com.dhitha.lms.auth.dto.AuthRequestDTO;
import com.dhitha.lms.auth.dto.AuthResponseDTO;
import com.dhitha.lms.auth.dto.UserDTO;
import com.dhitha.lms.auth.error.GenericException;
import com.nimbusds.jwt.JWTClaimsSet;
import feign.FeignException;
import feign.FeignException.FeignClientException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Implementation for {@link AuthService}
 *
 * @author Dhiraj
 */
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
      //TODO: Check for account expired, enabled and other details
      String idToken = tokenService.generateIdToken(userDTO);
      StringTokenizer stringTokenizer = new StringTokenizer(idToken, "\\.");
      return AuthResponseDTO.builder()
          .header(stringTokenizer.nextToken())
          .payload(stringTokenizer.nextToken())
          .signature(stringTokenizer.nextToken())
          .userDTO(userDTO)
          .build();
    } catch (FeignException.NotFound e) {
      log.warn("authenticate:(AuthRequestDTO) -> Invalid Credentials");
      // if below status is changed to 401, check https://github.com/OpenFeign/feign/issues/260
      // feign cannot send the response body which results in empty response
      throw new GenericException("Invalid Username / Password", HttpStatus.NOT_FOUND.value());
    } catch (NoSuchElementException | FeignClientException e) {
      log.error("authenticate:(AuthRequestDTO) ->  Error connecting to User Service: ", e);
      throw new GenericException();
    }
  }

  @Override
  public AuthResponseDTO verifyToken(String token) throws GenericException {
    JWTClaimsSet jwtClaimsSet = tokenService.verifyToken(token);
    log.info("Verification ClaimSet: {}", jwtClaimsSet.toJSONObject(true));

    try {
      UserDTO userDTO = new UserDTO();
      userDTO.setId(Long.valueOf(jwtClaimsSet.getSubject()));
      if (jwtClaimsSet.getStringClaim("roles") != null) {
        userDTO.setUserRoles(Arrays.asList(jwtClaimsSet.getStringClaim("roles").split(",")));
      }
      userDTO.setName(jwtClaimsSet.getStringClaim("name"));
      userDTO.setUsername(jwtClaimsSet.getStringClaim("username"));
      userDTO.setCreatedAt(jwtClaimsSet.getStringClaim("createdAt"));
      userDTO.setUpdatedAt(jwtClaimsSet.getStringClaim("updatedAt"));
      userDTO.setAccountNonExpired(jwtClaimsSet.getBooleanClaim("accountNonExpired"));
      userDTO.setAccountNonLocked(jwtClaimsSet.getBooleanClaim("accountNonLocked"));
      userDTO.setEnabled(jwtClaimsSet.getBooleanClaim("enabled"));
      userDTO.setCredentialsNonExpired(jwtClaimsSet.getBooleanClaim("credentialsNonExpired"));
      String idToken = tokenService.generateIdToken(userDTO, jwtClaimsSet.getIssueTime());
      StringTokenizer stringTokenizer = new StringTokenizer(idToken, "\\.");
      return AuthResponseDTO.builder()
          .header(stringTokenizer.nextToken())
          .payload(stringTokenizer.nextToken())
          .signature(stringTokenizer.nextToken())
          .userDTO(userDTO)
          .build();
    } catch (ParseException | NoSuchElementException e) {
      log.error("verifyToken(String) -> Exception while verifying token", e);
      throw new GenericException();
    }
  }
}
