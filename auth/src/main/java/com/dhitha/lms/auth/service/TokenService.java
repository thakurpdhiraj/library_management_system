package com.dhitha.lms.auth.service;

import com.dhitha.lms.auth.dto.UserDTO;
import com.dhitha.lms.auth.error.GenericException;
import com.nimbusds.jwt.JWTClaimsSet;
import java.util.Date;

/**
 * Service to handle JWT generation and verification using Nimbus Jose API
 *
 * @author Dhiraj
 */
public interface TokenService {

  String generateIdToken(UserDTO userDTO) throws GenericException;

  String generateIdToken(UserDTO userDTO, Date issueTime) throws GenericException;

  JWTClaimsSet verifyToken(String token) throws GenericException;

}
