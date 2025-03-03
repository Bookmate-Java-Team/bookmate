package com.bookmate.bookmate.user.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 액세스 토큰, 리프레시 토큰을 담아 전달하는 DTO
 */
@Getter
@Builder
public class TokenResponseDto {
  private String accessToken;
  private String refreshToken;
}
