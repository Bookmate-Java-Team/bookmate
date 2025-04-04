package com.bookmate.bookmate.chat.controller;

import com.bookmate.bookmate.chat.dto.request.ChatMessageRequestDto;
import com.bookmate.bookmate.chat.dto.request.CreateChatRoomRequestDto;
import com.bookmate.bookmate.chat.dto.response.ChatMessageResponseDto;
import com.bookmate.bookmate.chat.entity.ChatRoom;
import com.bookmate.bookmate.chat.service.ChatService;
import com.bookmate.bookmate.common.security.CustomUserDetails;
import com.bookmate.bookmate.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

  private final ChatService chatService;

  /**
   * 채팅방 생성(1:1 또는 그룹)
   */
  @PostMapping("/rooms")
  public ResponseEntity<ChatRoom> createChatRoom(
      @RequestBody @Valid CreateChatRoomRequestDto dto,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    User creator = userDetails.getUser();
    ChatRoom room = chatService.createChatRoom(dto, creator);
    return ResponseEntity.ok(room);
  }

  /**
   * 특정 채팅방의 메시지 목록 가져오기 (페이징)
   */
  @GetMapping("/rooms/{roomId}/messages")
  public ResponseEntity<Page<ChatMessageResponseDto>> getMessages(
      @PathVariable Long roomId,
      @AuthenticationPrincipal CustomUserDetails userDetails,
      Pageable pageable
  ) {
    User user = userDetails.getUser();
    Page<ChatMessageResponseDto> result = chatService.getMessages(roomId, pageable, user);
    return ResponseEntity.ok(result);
  }

  /**
   * 채팅방 떠나기
   */
  @DeleteMapping("/rooms/{roomId}")
  public ResponseEntity<Void> leaveRoom(
      @PathVariable Long roomId,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    User user = userDetails.getUser();
    chatService.leaveRoom(roomId, user);
    return ResponseEntity.ok().build();
  }

  /**
   * WebSocket 통해 메시지 전송
   */
  @MessageMapping("/chat/message")
  @SendTo("/topic/chatroom/{roomId}")
  // 실제론 roomId별 destination 구분: /topic/chatroom/{roomId} 등을 사용
  public ChatMessageResponseDto sendMessage(
      @Valid ChatMessageRequestDto dto,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    User user = userDetails.getUser();
    return chatService.sendMessage(dto, user);
  }
}
