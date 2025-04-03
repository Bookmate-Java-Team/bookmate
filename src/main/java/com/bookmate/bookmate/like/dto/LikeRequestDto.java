package com.bookmate.bookmate.like.dto;

import com.bookmate.bookmate.like.entity.enums.TargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeRequestDto {

  @NotNull
  @Schema(description = "좋아요 대상 ID", example = "1")
  private Long targetId;

  @NotNull
  @Schema(description = "좋아요 대상 타입 (POST 또는 COMMENT)", example = "POST")
  private TargetType targetType;
}
