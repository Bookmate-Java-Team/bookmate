package com.bookmate.bookmate.readingclub.dto.response;

import com.bookmate.bookmate.readingclub.entity.ReadingClubUser;
import com.bookmate.bookmate.readingclub.entity.enums.ReadingClubUserStatus;
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
  private Long userId;
  private String nickname;
  private ReadingClubUserStatus status;

  public static ReadingClubUserResponseDto fromEntity(ReadingClubUser rcu) {
    return ReadingClubUserResponseDto.builder()
        .userId(rcu.getUser().getId())
        .nickname(rcu.getUser().getNickname())
        .status(rcu.getStatus())
        .build();
  }
}
