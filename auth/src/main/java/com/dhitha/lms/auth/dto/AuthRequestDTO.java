package com.dhitha.lms.auth.dto;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Dhiraj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDTO {
  @NotEmpty
  private String username;
  @NotEmpty
  private String password;
}
