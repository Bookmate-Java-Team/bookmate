package com.bookmate.bookmate.user.service;

import com.bookmate.bookmate.user.dto.request.EmailVerifyRequestDto;
import com.bookmate.bookmate.user.dto.request.LoginRequestDto;
import com.bookmate.bookmate.user.dto.request.RegisterRequestDto;
import com.bookmate.bookmate.user.dto.request.SendEmailRequestDto;
import com.bookmate.bookmate.user.dto.request.UpdateUserRequestDto;
import com.bookmate.bookmate.user.dto.response.TokenResponseDto;
import com.bookmate.bookmate.user.dto.response.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

  // 이메일 인증 전 단계 - 이메일 중복체크, 인증코드 발송, Redis에 코드 저장
  void sendVerificationEmail(SendEmailRequestDto requestDto);

  // 이메일 인증 코드 검증
  boolean verifyEmailCode(EmailVerifyRequestDto requestDto);

  // 회원 가입
  UserResponseDto registerUser(RegisterRequestDto requestDto);

  // 로그인 - AccessToken, RefreshToken 반환
  TokenResponseDto login(LoginRequestDto requestDto);

  // 로그아웃 - Redis에 저장된 RefreshToken 제거
  void logout(Long userId);

  // 내 정보 조회
  UserResponseDto getMyInfo(Long userId);

  // 상대 회원 정보 조회
  UserResponseDto getUser(Long userId);

  // 전체 회원 조회 (관리자용)
  Page<UserResponseDto> getAllUsers(Pageable pageable);

  // 회원 정보 수정
  UserResponseDto updateUser(Long userId, UpdateUserRequestDto requestDto);

  // 회원 탈퇴 - soft delete
  void deleteUser(Long userId);

  // 액세스 토큰 재발급 (리프레시 토큰 함께 재발급)
  TokenResponseDto reissueTokens(String accessToken, String refreshToken);
}
