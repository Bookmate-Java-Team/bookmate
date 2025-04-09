package com.bookmate.bookmate.chat.repository;

import com.bookmate.bookmate.chat.entity.ChatParticipant;
import com.bookmate.bookmate.chat.entity.ChatRoom;
import com.bookmate.bookmate.readingclub.entity.ReadingClub;
import com.bookmate.bookmate.readingclub.entity.enums.ReadingClubUserStatus;
import com.bookmate.bookmate.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
  List<ChatParticipant> findByChatRoom(ChatRoom chatRoom);

  // 1:1 채팅 중복 확인 등
  Optional<ChatParticipant> findByChatRoomAndUser(ChatRoom chatRoom, User user);

  // 채팅방 참여자 수 조회
  long countByChatRoom(ChatRoom chatRoom);
}
