package com.bookmate.bookmate.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUpdateRequestDto {

  private String title;

  private String content;

  @Min(0)
  @Max(5)
  private Integer rating;
}
