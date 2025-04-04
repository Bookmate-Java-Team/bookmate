package com.bookmate.bookmate.chat.repository;

import com.bookmate.bookmate.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
  ChatRoom findByReadingClubId(Long readingClubId);
}
