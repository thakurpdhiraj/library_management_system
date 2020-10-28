package com.dhitha.lms.user.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing entity {@link com.dhitha.lms.user.entity.Role}
 *
 * @author Dhiraj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long id;

  private String name;
}
