package com.dhitha.lms.book.repository;

import com.dhitha.lms.book.dto.BookDTO;
import com.dhitha.lms.book.entity.Book;
import com.dhitha.lms.book.entity.Book_;
import com.dhitha.lms.book.entity.Category;
import com.dhitha.lms.book.entity.Category_;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

/**
 * Specification for fetching Book details
 *
 * @author Dhiraj
 */
@RequiredArgsConstructor
public class BookSpecification implements Specification<Book> {

  private final BookDTO bookDTO;

  @Override
  public Predicate toPredicate(
      @NonNull Root<Book> root,
      @NonNull CriteriaQuery<?> query,
      @NonNull CriteriaBuilder criteriaBuilder) {
    List<Predicate> predicates = new ArrayList<>();
    if (!Objects.isNull(bookDTO.getId())) {
      predicates.add(criteriaBuilder.equal(root.get(Book_.ID), bookDTO.getId()));
    }
    if (StringUtils.hasLength(bookDTO.getAuthor())) {
      predicates.add(
          criteriaBuilder.like(
              criteriaBuilder.lower(root.get(Book_.AUTHOR)),
              "%" + bookDTO.getAuthor().toLowerCase() + "%"));
    }
    if (StringUtils.hasLength(bookDTO.getName())) {
      predicates.add(
          criteriaBuilder.like(
              criteriaBuilder.lower(root.get(Book_.NAME)),
              "%" + bookDTO.getName().toLowerCase() + "%"));
    }
    if (StringUtils.hasLength(bookDTO.getIsbn())) {
      predicates.add(criteriaBuilder.equal(root.get(Book_.ISBN), bookDTO.getIsbn()));
    }
    if (StringUtils.hasLength(bookDTO.getPublication())) {
      predicates.add(
          criteriaBuilder.like(
              criteriaBuilder.lower(root.get(Book_.PUBLICATION)),
              "%" + bookDTO.getPublication().toLowerCase() + "%"));
    }
    if (!Objects.isNull(bookDTO.getPublicationYear())) {
      predicates.add(
          criteriaBuilder.equal(root.get(Book_.PUBLICATION_YEAR), bookDTO.getPublicationYear()));
    }
    if (!Objects.isNull(bookDTO.getCategory()) && !Objects.isNull(bookDTO.getCategory().getId())) {
      Join<Book, Category> category = root.join(Book_.CATEGORY);
      predicates.add(
          criteriaBuilder.equal(category.get(Category_.ID), bookDTO.getCategory().getId()));
    }
    return query.where(predicates.toArray(new Predicate[0])).getRestriction();
  }
}
