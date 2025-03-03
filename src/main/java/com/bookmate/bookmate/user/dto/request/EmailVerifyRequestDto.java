package com.bookmate.bookmate.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 이메일 인증코드 검증 시 사용되는 요청 DTO
 */
@Getter
@Setter
public class EmailVerifyRequestDto {

  @NotBlank
  @Email
  @Size(min = 3, max = 50)
  private String email;

  @NotBlank
  @Size(min = 8, max = 8)
  private String verificationCode;
}
