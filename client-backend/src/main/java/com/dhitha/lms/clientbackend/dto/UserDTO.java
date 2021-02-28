package com.dhitha.lms.clientbackend.dto;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
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
public class UserDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long id;

  @NotEmpty private String name;

  @NotEmpty private String email;

  @NotEmpty private String username;

  private String password;

  private Boolean accountNonExpired;

  private Boolean accountNonLocked;

  private Boolean credentialsNonExpired;

  private Boolean enabled;

  private String createdAt;

  private String updatedAt;

  private List<String> userRoles;
}
