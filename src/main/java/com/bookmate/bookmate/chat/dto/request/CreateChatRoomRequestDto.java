package com.bookmate.bookmate.chat.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 채팅방 생성/참여 요청 DTO
 */
@Getter
@Setter
@Builder
public class CreateChatRoomRequestDto {
  private Long targetUserId; // 1:1 채팅 대상 유저(옵션)
  private Long readingClubId; // 그룹채팅일 경우 연결할 독서모임(옵션)
}
