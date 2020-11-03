package com.dhitha.lms.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.io.Serializable;
import java.time.LocalDateTime;
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
