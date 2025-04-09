package com.bookmate.bookmate.chat.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * WebSocket STOMP로 메시지를 전송할 때 사용하는 요청 DTO
 */
@Getter
@Setter
public class ChatMessageRequestDto {
  private Long chatRoomId;
  private String content;
}
