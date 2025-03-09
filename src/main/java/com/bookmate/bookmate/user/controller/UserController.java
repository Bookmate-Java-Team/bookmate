package com.bookmate.bookmate.user.controller;

import com.bookmate.bookmate.common.security.CustomUserDetails;
import com.bookmate.bookmate.user.dto.request.EmailVerifyRequestDto;
import com.bookmate.bookmate.user.dto.request.LoginRequestDto;
import com.bookmate.bookmate.user.dto.request.RegisterRequestDto;
import com.bookmate.bookmate.user.dto.request.SendEmailRequestDto;
import com.bookmate.bookmate.user.dto.request.UpdateUserRequestDto;
import com.bookmate.bookmate.user.dto.response.TokenResponseDto;
import com.bookmate.bookmate.user.dto.response.UserResponseDto;
import com.bookmate.bookmate.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  /**
   * 이메일 인증코드 발송
   *
   * @param dto 이메일 주소가 담긴 요청 DTO
   * @return 200 OK
   */
  @PostMapping("/email")
  public ResponseEntity<Void> sendVerificationEmail(
      @RequestBody @Valid SendEmailRequestDto dto
  ) {
    userService.sendVerificationEmail(dto);
    return ResponseEntity.ok().build();
  }

  /**
   * 이메일 인증 코드 검증
   *
   * @param dto 이메일과 인증코드
   * @return true/false
   */
  @PostMapping("/email/verification")
  public ResponseEntity<Boolean> verifyEmailCode(
      @RequestBody @Valid EmailVerifyRequestDto dto
  ) {
    boolean result = userService.verifyEmailCode(dto);
    return ResponseEntity.ok(result);
  }

  /**
   * 회원가입
   *
   * @param dto 회원가입 요청 DTO
   * @return 가입된 사용자 정보
   */
  @PostMapping("/register")
  public ResponseEntity<UserResponseDto> registerUser(
      @RequestBody @Valid RegisterRequestDto dto
  ) {
    UserResponseDto response = userService.registerUser(dto);
    return ResponseEntity.ok(response);
  }

  /**
   * 로그인
   *
   * @param dto 로그인 요청 DTO
   * @return 액세스 토큰, 리프레시 토큰
   */
  @PostMapping("/login")
  public ResponseEntity<TokenResponseDto> login(
      @RequestBody @Valid LoginRequestDto dto
  ) {
    TokenResponseDto tokenResponse = userService.login(dto);
    return ResponseEntity.ok(tokenResponse);
  }

  /**
   * 로그아웃
   *
   * @param userDetails 인증된 사용자 정보
   * @return 200 OK
   */
  @PostMapping("/logout")
  public ResponseEntity<Void> logout(
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    Long userId = userDetails.getUser().getId();
    userService.logout(userId);
    return ResponseEntity.ok().build();
  }

  /**
   * 내 정보 조회
   *
   * @param userDetails 인증된 사용자 정보
   * @return 사용자 정보 DTO
   */
  @GetMapping("/me")
  public ResponseEntity<UserResponseDto> getMyInfo(
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    Long userId = userDetails.getUser().getId();
    UserResponseDto result = userService.getMyInfo(userId);
    return ResponseEntity.ok(result);
  }

  /**
   * 상대 회원 정보 조회
   *
   * @param userId 조회할 사용자 ID
   * @return 사용자 정보 DTO
   */
  @GetMapping("/{userId}")
  public ResponseEntity<UserResponseDto> getUser(
      @PathVariable Long userId
  ) {
    UserResponseDto result = userService.getUser(userId);
    return ResponseEntity.ok(result);
  }

  /**
   * 전체 회원 조회 (관리자 전용)
   *
   * @param page 페이지 번호
   * @param size 페이지 크기
   * @return 페이징된 사용자 정보 목록
   */
  @GetMapping("/all")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Page<UserResponseDto>> getAllUsers(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

    Page<UserResponseDto> result = userService.getAllUsers(pageable);
    return ResponseEntity.ok(result);
  }

  /**
   * 회원 정보 수정
   *
   * @param userDetails 로그인된 사용자
   * @param dto         수정할 정보 (비밀번호, 닉네임 등)
   * @return 수정된 사용자 정보
   */
  @PatchMapping
  public ResponseEntity<UserResponseDto> updateUser(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestBody @Valid UpdateUserRequestDto dto
  ) {
    Long userId = userDetails.getUser().getId();
    UserResponseDto result = userService.updateUser(userId, dto);
    return ResponseEntity.ok(result);
  }

  /**
   * 회원 탈퇴
   *
   * @param userDetails 로그인된 사용자
   * @return 200 OK
   */
  @DeleteMapping
  public ResponseEntity<Void> deleteUser(
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    Long userId = userDetails.getUser().getId();
    userService.deleteUser(userId);
    return ResponseEntity.ok().build();
  }

  /**
   * 액세스 토큰 재발급
   *
   * @param accessToken 헤더에서 받은 엑세스 토큰
   * @param refreshToken 헤더에서 받은 리프레시 토큰
   * @return 새 액세스 토큰, 리프레시 토큰
   */
  @PostMapping("/refresh-token")
  public ResponseEntity<TokenResponseDto> reissueTokens(
      @RequestHeader("Authorization") String accessToken,
      @RequestHeader("Refresh-Token") String refreshToken
  ) {
    if (accessToken.startsWith("Bearer ")) {
      accessToken = accessToken.substring(7);
    }
    TokenResponseDto response = userService.reissueTokens(accessToken, refreshToken);
    return ResponseEntity.ok(response);
  }
}