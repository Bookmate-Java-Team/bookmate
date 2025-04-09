package com.bookmate.bookmate.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class  CommentRequestDto {


  @Schema(description = "부모 댓글 ID (대댓글 작성 시 필요)", example = "1", nullable = true)
  private Long parentId;

  @NotBlank
  @Schema(description = "댓글 내용", example = "이 책 정말 좋네요!")
  private String content;
}
