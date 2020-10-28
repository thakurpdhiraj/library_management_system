package com.dhitha.lms.user.repository;

import com.dhitha.lms.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link User}
 *
 * @author Dhiraj
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Find a user by username
   *
   * @param username - unique username
   * @return {@link Optional} of user if present else {@link Optional#empty()}
   */
  Optional<User> findByUsername(String username);
}
