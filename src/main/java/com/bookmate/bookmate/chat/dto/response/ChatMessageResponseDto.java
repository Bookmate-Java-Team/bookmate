package com.bookmate.bookmate.chat.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 소켓으로 메시지를 브로드캐스트할 때 사용
 */
@Getter
@Setter
@Builder
public class ChatMessageResponseDto {
  private Long messageId;
  private Long chatRoomId;
  private Long senderId;
  private String senderNickname;
  private String content;
  private LocalDateTime createdAt;
}
