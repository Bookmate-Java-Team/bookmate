package com.bookmate.bookmate.book.dto;

import com.bookmate.bookmate.book.entity.Book;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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

  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate readDate;

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
