package com.bookmate.bookmate.chat.repository;

import com.bookmate.bookmate.chat.entity.ChatRoom;
import com.bookmate.bookmate.chat.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
  // 특정 채팅방의 메시지 페이징 조회
  Page<Message> findByChatRoomOrderByCreatedAtAsc(ChatRoom chatRoom, Pageable pageable);
}
