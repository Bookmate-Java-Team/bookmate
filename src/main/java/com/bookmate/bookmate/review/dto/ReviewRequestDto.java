package com.bookmate.bookmate.review.dto;

import com.bookmate.bookmate.book.entity.Book;
import com.bookmate.bookmate.book.entity.UserBookRecord;
import com.bookmate.bookmate.review.entity.Review;
import com.bookmate.bookmate.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRequestDto {

  @NotNull
  @Schema(description = "리뷰 제목", example = "흥미로운 책 리뷰")
  private String title;

  @NotNull
  @Schema(description = "리뷰 내용", example = "이 책은 정말 흥미로웠습니다...")
  private String content;

  @Min(0)
  @Max(5)
  @Schema(description = "책에 대한 평점 (0~5점)", example = "4")
  private Integer rating;

  public Review toEntity(UserBookRecord userBookRecord) {
    return Review.builder()
        .title(this.title)
        .content(this.content)
        .userBookRecord(userBookRecord)
        .rating(this.rating)
        .build();
  }

}
