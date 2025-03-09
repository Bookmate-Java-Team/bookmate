package com.bookmate.bookmate.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 이메일 인증코드 발송 시 사용되는 요청 DTO
 */
@Getter
@Setter
public class SendEmailRequestDto {

  @NotBlank
  @Email
  @Size(min = 3, max = 50)
  private String email;
}
