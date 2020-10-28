package com.dhitha.lms.book.repository;

import com.dhitha.lms.book.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Category}
 *
 * @author Dhiraj
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {}
