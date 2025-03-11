package com.bookmate.bookmate.like.dto;

import com.bookmate.bookmate.like.entity.Like;
import com.bookmate.bookmate.like.entity.enums.TargetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeResponseDto {

  private Long userId;
  private TargetType targetType;
  private Long targetId;

  public static LikeResponseDto toDto(Like like, Long userId) {
    return LikeResponseDto.builder().userId(userId).targetId(like.getTargetId())
        .targetType(like.getTargetType()).build();
  }
}
