package com.dhitha.lms.auth.service;

import com.dhitha.lms.auth.dto.UserDTO;
import com.dhitha.lms.auth.error.GenericException;

/**
 * Service to handle JWT generation and verification using Nimbus Jose API
 *
 * @author Dhiraj
 */
public interface TokenService {

  String generateIdToken(UserDTO userDTO) throws GenericException;

  UserDTO verifyToken(String token) throws GenericException;
}
