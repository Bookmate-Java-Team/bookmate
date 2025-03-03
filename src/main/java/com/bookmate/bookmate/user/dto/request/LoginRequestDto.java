package com.bookmate.bookmate.user.dto.request;

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
  private String email;

  @NotBlank
  @Size(max = 20)
  private String password;
}
