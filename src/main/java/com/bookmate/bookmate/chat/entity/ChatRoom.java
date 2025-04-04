package com.bookmate.bookmate.chat.entity;

import com.bookmate.bookmate.readingclub.entity.ReadingClub;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "chat_room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 100)
  private String name;

  @OneToOne
  @JoinColumn(name = "reading_club_id",
              unique = true,
              foreignKey = @ForeignKey(name = "fk_chatroom_readingclub_id"),
              nullable = true)
  private ReadingClub readingClub;

  private boolean isGroup;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public static ChatRoom createGroupRoom(ReadingClub club) {
    return ChatRoom.builder()
        .name(club.getTitle() + "채팅방")
        .readingClub(club)
        .isGroup(true)
        .build();
  }

  public static ChatRoom createPrivateRoom(String name) {
    return ChatRoom.builder()
        .name(name)
        .isGroup(false)
        .build();
  }
}
