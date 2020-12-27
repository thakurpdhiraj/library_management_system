package com.dhitha.lms.clientbackend.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** @author Dhiraj */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private String header;

  private String payload;

  private String signature;

  private UserDTO userDTO;
}
