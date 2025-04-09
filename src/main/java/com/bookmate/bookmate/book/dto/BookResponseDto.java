package com.bookmate.bookmate.book.dto;

import com.bookmate.bookmate.book.entity.Book;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponseDto {
  @Schema(description = "도서 ID", example = "1")
  private Long id;

  @Schema(description = "도서 제목", example = "자바의 정석")
  private String title;

  @Schema(description = "저자", example = "남궁성")
  private String author;

  @Schema(description = "ISBN 코드", example = "9788968481475")
  private String isbn;

  @Schema(description = "출판사", example = "도우출판")
  private String publisher;

  @Schema(description = "출판일", example = "2016-06-01")
  private String publishedDate;

  @Schema(description = "도서 설명", example = "자바의 기본 개념부터 고급 개념까지 설명한 책")
  private String description;

  @Schema(description = "도서 표지 이미지 URL", example = "https://image.aladin.co.kr/product/1475/cover.jpg")
  private String coverUrl;

  public static BookResponseDto toDto(Book book) {
    return BookResponseDto.builder()
        .id(book.getId())
        .title(book.getTitle())
        .author(book.getAuthor())
        .isbn(book.getIsbn())
        .publisher(book.getPublisher())
        .publishedDate(book.getPublishedDate())
        .description(book.getDescription())
        .coverUrl(book.getCoverUrl())
        .build();
  }

}
