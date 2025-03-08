package com.bookmate.bookmate.review.dto;

import com.bookmate.bookmate.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponseDto {

  private String title;
  private String content;
  private String isbn;
  private Integer rating;

  public static ReviewResponseDto toDto(Review review) {
    return ReviewResponseDto.builder()
        .title(review.getTitle())
        .content(review.getContent())
        .isbn(review.getUserBookRecord().getBook().getIsbn())
        .rating(review.getRating())
        .build();
  }

}
