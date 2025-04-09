package com.bookmate.bookmate.user.entity;

import com.bookmate.bookmate.user.entity.enums.RoleType;
import com.bookmate.bookmate.user.entity.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 50)
  private String email;

  @Column(nullable = false, length = 100)
  private String password;

  @Column(nullable = false, unique = true, length = 20)
  private String nickname;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RoleType role = RoleType.USER;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserStatus status = UserStatus.ACTIVE;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Builder
  public User(String email, String password, String nickname, RoleType role) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.role = role;
  }

  public void deactivate() {
    this.status = UserStatus.DEACTIVATED;
    this.deletedAt = LocalDateTime.now();
  }
}
