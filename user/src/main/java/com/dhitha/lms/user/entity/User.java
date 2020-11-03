package com.dhitha.lms.user.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Entity to represent user of LMS
 *
 * @author Dhiraj
 */
@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "username", nullable = false, unique = true, updatable = false)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "account_non_expired", columnDefinition = "BIT(1) DEFAULT 1")
  private Boolean accountNonExpired;

  @Column(name = "account_non_locked", columnDefinition = "BIT(1) DEFAULT 1")
  private Boolean accountNonLocked;

  @Column(name = "credentials_non_expired", columnDefinition = "BIT(1) DEFAULT 1")
  private Boolean credentialsNonExpired;

  @Column(name = "enabled", columnDefinition = "BIT(1) DEFAULT 1")
  private Boolean enabled;

  @Column(name = "created_at", columnDefinition = "TIMESTAMP")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @ElementCollection(targetClass = Role.class)
  @Enumerated(EnumType.STRING)
  @CollectionTable(name = "user_roles")
  @Column(name = "user_role")
  private List<Role> userRoles;
}
