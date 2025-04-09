package com.bookmate.bookmate.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateRequestDto {

  @Schema(description = "제목", example = "책에 대한 나의 견해", nullable = true)
  private String title;

  @Schema(description = "내용", example = "나의 생각은...", nullable = true)
  private String content;

}
