package com.bookmate.bookmate.readingclub.dto.response;

import com.bookmate.bookmate.readingclub.entity.ReadingClubUser;
import com.bookmate.bookmate.readingclub.entity.enums.ReadingClubUserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 특정 모임에 가입/신청한 사용자 응답 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadingClubUserResponseDto {
  @Schema(description = "사용자 ID", example = "10")
  private Long userId;

  @Schema(description = "사용자 닉네임", example = "독서광")
  private String nickname;

  @Schema(description = "사용자의 모임 내 상태", example = "MEMBER")
  private ReadingClubUserStatus status;

  public static ReadingClubUserResponseDto fromEntity(ReadingClubUser rcu) {
    return ReadingClubUserResponseDto.builder()
        .userId(rcu.getUser().getId())
        .nickname(rcu.getUser().getNickname())
        .status(rcu.getStatus())
        .build();
  }
}
