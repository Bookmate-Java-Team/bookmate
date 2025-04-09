package com.bookmate.bookmate.review.dto;

import com.bookmate.bookmate.review.entity.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponseDto {

  @Schema(description = "리뷰 제목", example = "흥미로운 책 리뷰")
  private String title;

  @Schema(description = "리뷰 내용", example = "이 책은 정말 흥미로웠습니다...")
  private String content;

  @Schema(description = "도서 ISBN 번호", example = "978-3-16-148410-0")
  private String isbn;

  @Schema(description = "책에 대한 평점 (0~5점)", example = "4")
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
