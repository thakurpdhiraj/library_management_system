package com.dhitha.lms.auth.service;

import com.dhitha.lms.auth.dto.AuthRequestDTO;
import com.dhitha.lms.auth.dto.AuthResponseDTO;
import com.dhitha.lms.auth.dto.UserDTO;
import com.dhitha.lms.auth.error.GenericException;

/**
 * Service for Authentication
 * @author Dhiraj
 */
public interface AuthService {

  AuthResponseDTO authenticate(AuthRequestDTO userDTO) throws GenericException;

  AuthResponseDTO verifyToken(String token) throws GenericException;
}
