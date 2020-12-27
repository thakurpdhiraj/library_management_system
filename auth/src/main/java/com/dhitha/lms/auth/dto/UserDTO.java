package com.dhitha.lms.auth.dto;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for exchange with User service
 *
 * @author Dhiraj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long id;

  private String name;

  private String email;

  private String username;

  private Boolean accountNonExpired;

  private Boolean accountNonLocked;

  private Boolean credentialsNonExpired;

  private Boolean enabled;

  private String createdAt;

  private String updatedAt;

  private List<String> userRoles;
}
