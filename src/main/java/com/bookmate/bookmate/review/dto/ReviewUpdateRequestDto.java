package com.bookmate.bookmate.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUpdateRequestDto {

  @Schema(description = "리뷰 제목 (변경할 경우 입력)", example = "업데이트된 리뷰 제목")
  private String title;

  @Schema(description = "리뷰 내용 (변경할 경우 입력)", example = "업데이트된 리뷰 내용입니다.")
  private String content;

  @Min(0)
  @Max(5)
  @Schema(description = "책에 대한 평점 (0~5점)", example = "3")
  private Integer rating;
}
