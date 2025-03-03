package com.bookmate.bookmate.user.service;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.common.error.exception.UnauthorizedException;
import com.bookmate.bookmate.common.security.JwtTokenProvider;
import com.bookmate.bookmate.user.dto.request.EmailVerifyRequestDto;
import com.bookmate.bookmate.user.dto.request.LoginRequestDto;
import com.bookmate.bookmate.user.dto.request.RegisterRequestDto;
import com.bookmate.bookmate.user.dto.request.SendEmailRequestDto;
import com.bookmate.bookmate.user.dto.request.UpdateUserRequestDto;
import com.bookmate.bookmate.user.dto.response.TokenResponseDto;
import com.bookmate.bookmate.user.dto.response.UserResponseDto;
import com.bookmate.bookmate.user.entity.User;
import com.bookmate.bookmate.user.entity.enums.RoleType;
import com.bookmate.bookmate.user.entity.enums.UserStatus;
import com.bookmate.bookmate.user.exception.EmailDuplicateException;
import com.bookmate.bookmate.user.exception.NicknameDuplicateException;
import com.bookmate.bookmate.user.exception.UserBadRequestException;
import com.bookmate.bookmate.user.exception.UserDeactivatedException;
import com.bookmate.bookmate.user.exception.UserNotFoundException;
import com.bookmate.bookmate.user.repository.RefreshTokenRepository;
import com.bookmate.bookmate.user.repository.UserRepository;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

  private static final String EMAIL_VERIFICATION_PREFIX = "EMAIL_VERIF:";

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final StringRedisTemplate redisTemplate;
  private final MailSender mailSender;
  private final RefreshTokenRepository refreshTokenRepository;

  private User findUserById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));
  }

  // 이메일 인증 전 단계 - 이메일 중복체크, 인증메일 발송, Redis에 코드 저장
  @Override
  public void sendVerificationEmail(SendEmailRequestDto requestDto) {
    String email = requestDto.getEmail();
    if (userRepository.findByEmail(email).isPresent()) {
      throw new EmailDuplicateException(email);
    }

    String verificationCode = UUID.randomUUID().toString().substring(0, 8);

    ValueOperations<String, String> ops = redisTemplate.opsForValue();
    ops.set(EMAIL_VERIFICATION_PREFIX + email, verificationCode, Duration.ofMinutes(5));

    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(email);
    mailMessage.setSubject("[Bookmate] 이메일 인증 코드 안내");
    mailMessage.setText("인증코드는 " + verificationCode + " 입니다.");
    mailSender.send(mailMessage);
  }

  // 이메일 인증 코드 검증
  @Override
  public boolean verifyEmailCode(EmailVerifyRequestDto requestDto) {
    String key = EMAIL_VERIFICATION_PREFIX + requestDto.getEmail();
    ValueOperations<String, String> ops = redisTemplate.opsForValue();
    String savedCode = ops.get(key);
    if (savedCode != null && savedCode.equals(requestDto.getVerificationCode())) {
      redisTemplate.delete(key);
      return true;
    }
    return false;
  }

  // 회원 가입
  @Override
  public UserResponseDto registerUser(RegisterRequestDto requestDto) {
    if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
      throw new EmailDuplicateException(requestDto.getEmail());
    }
    if (userRepository.findByNickname(requestDto.getNickname()).isPresent()) {
      throw new NicknameDuplicateException(requestDto.getNickname());
    }

    User user = User.builder()
        .email(requestDto.getEmail())
        .password(passwordEncoder.encode(requestDto.getPassword()))
        .nickname(requestDto.getNickname())
        .role(RoleType.USER)
        .build();

    userRepository.save(user);

    return UserResponseDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .role(user.getRole())
        .createdAt(user.getCreatedAt())
        .build();
  }

  // 로그인 - AccessToken, RefreshToken 반환
  @Override
  public TokenResponseDto login(LoginRequestDto requestDto) {
    User user = userRepository.findByEmail(requestDto.getEmail())
        .orElseThrow(() -> new UserBadRequestException(ErrorCode.WRONG_EMAIL));

    if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
      throw new UnauthorizedException(ErrorCode.PASSWORD_MISMATCH);
    }

    if (user.getStatus() == UserStatus.DEACTIVATED) {
      throw new UserDeactivatedException();
    }

    String accessToken = jwtTokenProvider.createAccessToken(user);
    String refreshToken = jwtTokenProvider.createRefreshToken(user);

    String hashedRefreshToken = passwordEncoder.encode(refreshToken);

    refreshTokenRepository.save(
        user.getId(),
        hashedRefreshToken,
        Duration.ofMillis(jwtTokenProvider.getRemainMillisecond(refreshToken))
    );

    return TokenResponseDto.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  // 로그아웃 - Redis에 저장된 RefreshToken 제거
  @Override
  public void logout(Long userId) {
    refreshTokenRepository.delete(userId);
  }

  // 내 정보 조회
  @Override
  public UserResponseDto getMyInfo(Long userId) {
    User user = findUserById(userId);
    return UserResponseDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .role(user.getRole())
        .status(user.getStatus())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .build();
  }

  // 상대 회원 정보 조회
  @Override
  public UserResponseDto getUser(Long userId) {
    User user = findUserById(userId);
    return UserResponseDto.builder()
        .id(user.getId())
        .nickname(user.getNickname())
        .role(user.getRole())
        .status(user.getStatus())
        .build();
  }

  // 전체 회원 조회 (관리자용)
  @Override
  public Page<UserResponseDto> getAllUsers(Pageable pageable) {
    Page<User> userPage = userRepository.findAll(pageable);

    return userPage.map(user -> UserResponseDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .role(user.getRole())
        .status(user.getStatus())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .deletedAt(user.getDeletedAt())
        .build());
  }

  // 회원 정보 수정
  @Override
  public UserResponseDto updateUser(Long userId, UpdateUserRequestDto requestDto) {
    User user = findUserById(userId);

    boolean isNicknameUpdated = false;
    boolean isPasswordUpdated = false;

    if (requestDto.getNickname() != null) {
      Optional<User> existingUser = userRepository.findByNickname(requestDto.getNickname());
      if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
        throw new NicknameDuplicateException(requestDto.getNickname());
      }
      user.setNickname(requestDto.getNickname());
      isNicknameUpdated = true;
    }

    if (requestDto.getPassword() != null || requestDto.getNewPassword() != null) {
      if (requestDto.getPassword() == null || requestDto.getNewPassword() == null) {
        throw new UserBadRequestException(ErrorCode.INVALID_INPUT_VALUE);
      }
      if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
        throw new UnauthorizedException(ErrorCode.PASSWORD_MISMATCH);
      }
      user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
      isPasswordUpdated = true;
    }

    if (!isNicknameUpdated && !isPasswordUpdated) {
      throw new UserBadRequestException(ErrorCode.INVALID_INPUT_VALUE);
    }

    return UserResponseDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .role(user.getRole())
        .status(user.getStatus())
        .build();
  }

  // 회원 탈퇴 - soft delete
  @Override
  public void deleteUser(Long userId) {
    User user = findUserById(userId);
    user.deactivate();
  }

  // 액세스 토큰 재발급 (리프레시 토큰 함께 재발급)
  @Override
  public TokenResponseDto reissueTokens(String accessToken, String refreshToken) {
    // Access Token 유효성 검증 - 여전히 유효하다면 만료 전이므로 재발급 거부
    if (jwtTokenProvider.validateToken(accessToken)) {
      throw new UserBadRequestException(ErrorCode.ACCESS_TOKEN_NOT_EXPIRED_YET);
    }

    // Refresh Token 유효성 검증
    if (!jwtTokenProvider.validateToken(refreshToken)) {
      throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN);
    }

    Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

    // Redis에 저장된 리프레시 토큰 일치 확인 (일치하지 않을 시 탈취 위험으로 Redis에서 토큰 삭제)
    String StoredHashedRt = refreshTokenRepository.get(userId);
    if (StoredHashedRt == null || !passwordEncoder.matches(refreshToken, StoredHashedRt)) {
      refreshTokenRepository.delete(userId);
      throw new UnauthorizedException(ErrorCode.REFRESH_TOKEN_MISMATCH);
    }

    User user = findUserById(userId);

    String newAccessToken = jwtTokenProvider.createAccessToken(user);
    String newRefreshToken = jwtTokenProvider.createRefreshToken(user);

    String newHashedRt = passwordEncoder.encode(newRefreshToken);

    refreshTokenRepository.save(
        userId,
        newHashedRt,
        Duration.ofMillis(jwtTokenProvider.getRemainMillisecond(newRefreshToken))
    );

    return TokenResponseDto.builder()
        .accessToken(newAccessToken)
        .refreshToken(newRefreshToken)
        .build();
  }
}
