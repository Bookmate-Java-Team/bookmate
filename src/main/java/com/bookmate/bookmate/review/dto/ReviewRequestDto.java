package com.bookmate.bookmate.review.dto;

import com.bookmate.bookmate.review.entity.Review;
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
  private String title;

  @NotNull
  private String content;

  @NotNull
  private String isbn;

  @Min(0)
  @Max(5)
  private Integer rating;

  public Review toEntity() {
    return Review.builder()
        .title(this.title)
        .content(this.content)
        .isbn(this.isbn)
        .rating(this.rating)
        .build();
  }

}
