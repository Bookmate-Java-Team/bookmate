package com.bookmate.bookmate.chat.service;

import com.bookmate.bookmate.chat.dto.request.ChatMessageRequestDto;
import com.bookmate.bookmate.chat.dto.request.CreateChatRoomRequestDto;
import com.bookmate.bookmate.chat.dto.response.ChatMessageResponseDto;
import com.bookmate.bookmate.chat.entity.ChatParticipant;
import com.bookmate.bookmate.chat.entity.ChatRoom;
import com.bookmate.bookmate.chat.entity.Message;
import com.bookmate.bookmate.chat.exception.ChatRoomNotFoundException;
import com.bookmate.bookmate.chat.repository.ChatParticipantRepository;
import com.bookmate.bookmate.chat.repository.ChatRoomRepository;
import com.bookmate.bookmate.chat.repository.MessageRepository;
import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.common.error.exception.ForbiddenException;
import com.bookmate.bookmate.readingclub.entity.ReadingClub;
import com.bookmate.bookmate.readingclub.exception.ReadingClubNotFoundException;
import com.bookmate.bookmate.readingclub.repository.ReadingClubRepository;
import com.bookmate.bookmate.user.entity.User;
import com.bookmate.bookmate.user.exception.UserNotFoundException;
import com.bookmate.bookmate.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl implements ChatService {

  private final ChatRoomRepository chatRoomRepository;
  private final ChatParticipantRepository chatParticipantRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ReadingClubRepository readingClubRepository;

  // 1:1 채팅 또는 그룹 채팅방 생성
  @Override
  public ChatRoom createChatRoom(CreateChatRoomRequestDto dto, User creator) {
    // 그룹 채팅방
    if (dto.getReadingClubId() != null) {
      ReadingClub club = readingClubRepository.findById(dto.getReadingClubId())
          .orElseThrow(() -> new ReadingClubNotFoundException(dto.getReadingClubId()));
      ChatRoom exist = chatRoomRepository.findByReadingClubId(club.getId());
      if (exist != null) {
        return exist;
      }

      ChatRoom groupRoom = ChatRoom.createGroupRoom(club);
      chatRoomRepository.save(groupRoom);

      ChatParticipant hostParticipant = ChatParticipant.of(groupRoom, creator);
      chatParticipantRepository.save(hostParticipant);

      return groupRoom;
    }
    // 1:1 채팅방
    else {
      if (dto.getTargetUserId() == null) {
        throw new ForbiddenException(ErrorCode.INVALID_INPUT_VALUE);
      }
      User target = userRepository.findById(dto.getTargetUserId())
          .orElseThrow(() -> new UserNotFoundException(dto.getTargetUserId()));

      String roomName = creator.getNickname() + "-" + target.getNickname();
      ChatRoom privateRoom = ChatRoom.createPrivateRoom(roomName);
      chatRoomRepository.save(privateRoom);

      chatParticipantRepository.save(ChatParticipant.of(privateRoom, creator));
      chatParticipantRepository.save(ChatParticipant.of(privateRoom, target));

      return privateRoom;
    }
  }

  // 채팅방에 메시지 보냄
  @Override
  public ChatMessageResponseDto sendMessage(ChatMessageRequestDto dto, User sender) {
    ChatRoom chatRoom = chatRoomRepository.findById(dto.getChatRoomId())
        .orElseThrow(() -> new ChatRoomNotFoundException(dto.getChatRoomId()));

    chatParticipantRepository.findByChatRoomAndUser(chatRoom, sender)
        .orElseThrow(() -> new ForbiddenException(ErrorCode.HANDLE_ACCESS_DENIED));

    Message message = Message.create(chatRoom, sender, dto.getContent());
    messageRepository.save(message);

    return ChatMessageResponseDto.builder()
        .messageId(message.getId())
        .chatRoomId(chatRoom.getId())
        .senderId(sender.getId())
        .senderNickname(sender.getNickname())
        .content(message.getContent())
        .createdAt(message.getCreatedAt())
        .build();
  }

  // 특정 채팅방 메시지 페이징 조회
  @Override
  @Transactional(readOnly = true)
  public Page<ChatMessageResponseDto> getMessages(Long chatRoomId, Pageable pageable, User requester) {
    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new ChatRoomNotFoundException(chatRoomId));

    chatParticipantRepository.findByChatRoomAndUser(chatRoom, requester)
        .orElseThrow(() -> new ForbiddenException(ErrorCode.HANDLE_ACCESS_DENIED));

    Page<Message> messagePage = messageRepository.findByChatRoomOrderByCreatedAtAsc(chatRoom, pageable);

    return messagePage.map(msg -> ChatMessageResponseDto.builder()
        .messageId(msg.getId())
        .chatRoomId(chatRoom.getId())
        .senderId(msg.getSender().getId())
        .senderNickname(msg.getSender().getNickname())
        .content(msg.getContent())
        .createdAt(msg.getCreatedAt())
        .build());
  }

  // 독서 모임 삭제 시 해당 채팅방 삭제
  @Override
  public void deleteChatRoomByReadingClub(Long readingClubId) {
    ChatRoom chatRoom = chatRoomRepository.findByReadingClubId(readingClubId);
    if (chatRoom != null) {
      List<ChatParticipant> participants = chatParticipantRepository.findByChatRoom(chatRoom);
      chatParticipantRepository.deleteAll(participants);
      chatRoomRepository.delete(chatRoom);
    }
  }

  // 그룹 채팅방 초대
  @Override
  public void inviteChatRoom(Long readingClubId, User user) {
    ChatRoom chatRoom = chatRoomRepository.findByReadingClubId(readingClubId);
    if (chatRoom == null) {
      throw new ChatRoomNotFoundException(readingClubId);
    }

    chatParticipantRepository.save(ChatParticipant.of(chatRoom, user));
  }

  // 채팅방 떠나기
  @Override
  public void leaveRoom(Long chatRoomId, User user) {
    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new ChatRoomNotFoundException(chatRoomId));

    ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoomAndUser(chatRoom, user)
        .orElseThrow(() -> new ForbiddenException(ErrorCode.HANDLE_ACCESS_DENIED));

    chatParticipantRepository.delete(chatParticipant);

    // chatRoom에 참여자가 없을 경우 chatRoom 삭제
    long participantCount = chatParticipantRepository.countByChatRoom(chatRoom);
    if (participantCount == 0) {
      chatRoomRepository.delete(chatRoom);
    }
  }
}
