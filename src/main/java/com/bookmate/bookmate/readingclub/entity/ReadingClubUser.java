package com.bookmate.bookmate.readingclub.entity;

import com.bookmate.bookmate.readingclub.entity.enums.ReadingClubUserStatus;
import com.bookmate.bookmate.user.entity.User;
import com.bookmate.bookmate.user.entity.enums.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "reading_club_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ReadingClubUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reading_club_id", nullable = false)
  private ReadingClub readingClub;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReadingClubUserStatus status = ReadingClubUserStatus.PENDING;

  @CreatedDate
  @Column(name = "joined_at", updatable = false)
  private LocalDateTime joinedAt;

  public void accept() {
    this.status = ReadingClubUserStatus.ACCEPTED;
  }

  public void reject() {
    this.status = ReadingClubUserStatus.REJECTED;
  }
}
