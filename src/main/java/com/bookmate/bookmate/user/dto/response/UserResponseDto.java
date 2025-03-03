package com.bookmate.bookmate.user.dto.response;

import com.bookmate.bookmate.user.entity.enums.RoleType;
import com.bookmate.bookmate.user.entity.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

/**
 * 사용자 정보 응답 DTO
 */
@Getter
@JsonInclude(Include.NON_NULL)
public class UserResponseDto {
  private Long id;
  private String email;
  private String nickname;
  private RoleType role;
  private UserStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime deletedAt;

  @Builder
  public UserResponseDto(
      Long id,
      String email,
      String nickname,
      UserStatus status,
      RoleType role,
      LocalDateTime createdAt,
      LocalDateTime updatedAt,
      LocalDateTime deletedAt
  ) {
      this.id = id;
      this.email = email;
      this.nickname = nickname;
      this.status = status;
      this.role = role;
      this.createdAt = createdAt;
      this.updatedAt = updatedAt;
      this.deletedAt = deletedAt;
  }
}
