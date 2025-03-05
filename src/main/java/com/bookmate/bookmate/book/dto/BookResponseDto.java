package com.bookmate.bookmate.book.dto;

import com.bookmate.bookmate.book.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponseDto {
  private Long id;
  private String title;
  private String author;
  private String isbn;
  private String publisher;
  private String publishedDate;
  private String description;
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
