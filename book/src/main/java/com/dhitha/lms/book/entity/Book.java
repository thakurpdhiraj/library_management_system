package com.dhitha.lms.book.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Entity to represent books in LMS
 *
 * @author Dhiraj
 */
@Entity
@Table(name = "book")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "isbn", nullable = false, unique = true)
  private String isbn;

  @Column(name = "name", nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @Column(name = "author", nullable = false)
  private String author;

  @Column(name = "publication", nullable = false)
  private String publication;

  @Column(name = "publication_year")
  private Integer publicationYear;

  @Column(name = "pages", nullable = false)
  private Integer pages;

  @Lob
  @Column(name = "summary")
  private String summary;

  @CreationTimestamp
  @Column(name = "added_at")
  private LocalDateTime addedAt;
}
