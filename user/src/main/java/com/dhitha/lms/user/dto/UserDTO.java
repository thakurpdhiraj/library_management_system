package com.dhitha.lms.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing entity {@link com.dhitha.lms.user.entity.User}
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

  @NotEmpty private String name;

  @NotEmpty private String email;

  @NotEmpty private String username;

  @JsonProperty(access = Access.WRITE_ONLY)
  @NotEmpty
  private String password;

  private Boolean accountNonExpired;

  private Boolean accountNonLocked;

  private Boolean credentialsNonExpired;

  private Boolean enabled;

  @JsonProperty(access = Access.READ_ONLY)
  private LocalDateTime createdAt;

  @JsonProperty(access = Access.READ_ONLY)
  private LocalDateTime updatedAt;

  @NotNull private List<String> userRoles;
}
