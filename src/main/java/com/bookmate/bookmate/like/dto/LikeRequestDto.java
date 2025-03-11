package com.bookmate.bookmate.like.dto;

import com.bookmate.bookmate.like.entity.enums.TargetType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeRequestDto {

  @NotNull
  private Long targetId;
  @NotNull
  private TargetType targetType;
}
