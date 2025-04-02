package com.bookmate.bookmate.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 요청 DTO
 */
@Getter
@Setter
public class LoginRequestDto {

  @NotBlank
  @Email
  @Schema(description = "사용자 이메일 (로그인 ID)", example = "user@example.com")
  private String email;

  @NotBlank
  @Size(max = 20)
  @Schema(description = "비밀번호 (최대 20자)", example = "Pa$$w0rd!")
  private String password;
}
