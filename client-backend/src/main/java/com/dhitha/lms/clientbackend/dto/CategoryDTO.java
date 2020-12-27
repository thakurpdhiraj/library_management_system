package com.dhitha.lms.clientbackend.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for exchange with Category Service
 *
 * @author Dhiraj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private Integer id;

  private String name;
}
