package com.bookmate.bookmate.book.dto;

import com.bookmate.bookmate.book.entity.Book;
import io.swagger.v3.oas.annotations.media.Schema;
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
  @Schema(description = "도서 제목", example = "자바의 정석")
  private String title;

  @NotBlank
  @Schema(description = "저자", example = "남궁성")
  private String author;

  @NotBlank
  @Schema(description = "ISBN 코드", example = "9788968481475")
  private String isbn;

  @NotBlank
  @Schema(description = "출판사", example = "도우출판")
  private String publisher;

  @NotBlank
  @Schema(description = "출판일", example = "2016-06-01")
  private String publishedDate;

  @Schema(description = "도서 설명", example = "자바의 기본 개념부터 고급 개념까지 설명한 책")
  private String description;

  @Schema(description = "도서 표지 이미지 URL", example = "https://image.aladin.co.kr/product/1475/cover.jpg")
  private String coverUrl;

  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Schema(description = "읽은 날짜", example = "2025-03-28")
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
