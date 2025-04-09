package com.bookmate.bookmate.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 액세스 토큰, 리프레시 토큰을 담아 전달하는 DTO
 */
@Getter
@Builder
public class TokenResponseDto {
  @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6...")
  private String accessToken;
  @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6...")
  private String refreshToken;
}
