package com.bookmate.bookmate.like.entity.enums;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "좋아요 대상 타입")
public enum TargetType {
  @Schema(description = "게시글", example = "POST")
  POST,

  @Schema(description = "댓글", example = "COMMENT")
  COMMENT
}
