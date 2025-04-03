package com.bookmate.bookmate.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원가입 요청 DTO
 */
@Getter
@Setter
public class RegisterRequestDto {

  @NotBlank
  @Email
  @Size(min = 3, max = 50)
  @Schema(description = "사용자 이메일 (로그인 ID)", example = "user@example.com")
  private String email;

  @NotBlank
  @Size(min = 8, max = 20, message = "비밀번호는 8~20자리여야 합니다.")
  @Pattern(
      regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,20}$",
      message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 포함해야 합니다."
  )
  @Schema(description = "비밀번호 (영문, 숫자, 특수문자 포함)", example = "Pa$$w0rd!")
  private String password;

  @NotBlank
  @Size(min = 2, max = 20)
  @Schema(description = "사용자 닉네임 (2~20자)", example = "책읽는코더")
  private String nickname;
}
