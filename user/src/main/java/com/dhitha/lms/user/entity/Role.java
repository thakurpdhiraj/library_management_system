package com.dhitha.lms.user.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

/**
 * Role of a user
 *
 * @author Dhiraj
 */
public enum  Role {
  USER,
  ADMIN
}
