package com.bookmate.bookmate.book.dto;

import com.bookmate.bookmate.book.entity.Book;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookRequestDto {
  @NotBlank
  private String title;

  @NotBlank
  private String author;

  @NotBlank
  private String isbn;

  @NotBlank
  private String publisher;

  @NotBlank
  private String publishedDate;

  private String description;

  private String coverUrl;

  public Book toEntity() {
    return Book.builder()
        .title(this.title)
        .author(this.author)
        .isbn(this.isbn)
        .publisher(this.publisher)
        .publishedDate(this.publishedDate)
        .description(this.description)
        .coverUrl(this.coverUrl)
        .build();
  }
}
