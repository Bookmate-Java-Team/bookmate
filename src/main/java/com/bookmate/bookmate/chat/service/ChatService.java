package com.bookmate.bookmate.chat.service;

import com.bookmate.bookmate.chat.dto.request.ChatMessageRequestDto;
import com.bookmate.bookmate.chat.dto.request.CreateChatRoomRequestDto;
import com.bookmate.bookmate.chat.dto.response.ChatMessageResponseDto;
import com.bookmate.bookmate.chat.entity.ChatRoom;
import com.bookmate.bookmate.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatService {

  // 1:1 채팅 또는 그룹 채팅방 생성
  ChatRoom createChatRoom(CreateChatRoomRequestDto dto, User creator);

  // 채팅방에 메시지 보냄
  ChatMessageResponseDto sendMessage(ChatMessageRequestDto dto, User sender);

  // 특정 채팅방 메시지 페이징 조회
  Page<ChatMessageResponseDto> getMessages(Long chatRoomId, Pageable pageable, User requester);

  // 독서 모임 삭제 시 해당 채팅방 삭제
  void deleteChatRoomByReadingClub(Long readingClubId);

  // 그룹 채팅방 초대
  void inviteChatRoom(Long readingClubId, User user);

  // 채팅방 떠나기
  void leaveRoom(Long chatRoomId, User user);
}
