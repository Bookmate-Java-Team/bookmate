package com.bookmate.bookmate.readingclub.controller;

import com.bookmate.bookmate.common.security.CustomUserDetails;
import com.bookmate.bookmate.readingclub.dto.request.ReadingClubRequestDto;
import com.bookmate.bookmate.readingclub.dto.response.ReadingClubResponseDto;
import com.bookmate.bookmate.readingclub.dto.response.ReadingClubUserResponseDto;
import com.bookmate.bookmate.readingclub.entity.enums.ReadingClubUserStatus;
import com.bookmate.bookmate.readingclub.service.ReadingClubJoinService;
import com.bookmate.bookmate.readingclub.service.ReadingClubService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reading-clubs")
@RequiredArgsConstructor
public class ReadingClubController {

  private final ReadingClubService readingClubService;
  private final ReadingClubJoinService readingClubJoinService;

  /**
   * 독서모임 생성
   */
  @PostMapping
  public ResponseEntity<ReadingClubResponseDto> createReadingClub(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestBody @Valid ReadingClubRequestDto dto
  ) {
    Long hostUserId = userDetails.getUser().getId();
    ReadingClubResponseDto response = readingClubService.createReadingClub(hostUserId, dto);
    return ResponseEntity.ok(response);
  }

  /**
   * 독서모임 수정
   */
  @PatchMapping("/{clubId}")
  public ResponseEntity<ReadingClubResponseDto> updateReadingClub(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long clubId,
      @RequestBody @Valid ReadingClubRequestDto dto
  ) {
    Long hostUserId = userDetails.getUser().getId();
    ReadingClubResponseDto response = readingClubService.updateReadingClub(hostUserId, clubId, dto);
    return ResponseEntity.ok(response);
  }

  /**
   * 독서모임 삭제
   */
  @DeleteMapping("/{clubId}")
  public ResponseEntity<Void> deleteReadingClub(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long clubId
  ) {
    Long hostUserId = userDetails.getUser().getId();
    readingClubService.deleteReadingClub(hostUserId, clubId);
    return ResponseEntity.ok().build();
  }

  /**
   * 특정 유저가 가입한 독서모임 목록 조회 (페이징)
   */
  @GetMapping("/user/{userId}")
  public ResponseEntity<Page<ReadingClubResponseDto>> getReadingClubsByUser(
      @PathVariable Long userId,
      Pageable pageable
  ) {
    return ResponseEntity.ok(readingClubService.getReadingClubsByUser(userId, pageable));
  }

  /**
   * 전체 독서모임 목록 조회  (페이징)
   */
  @GetMapping
  public ResponseEntity<Page<ReadingClubResponseDto>> getAllReadingClubs(Pageable pageable) {
    return ResponseEntity.ok(readingClubService.getAllReadingClubs(pageable));
  }

  /**
   * 특정 독서모임 조회
   */
  @GetMapping("/{clubId}")
  public ResponseEntity<ReadingClubResponseDto> getReadingClub(@PathVariable Long clubId) {
    return ResponseEntity.ok(readingClubService.getReadingClub(clubId));
  }

  /**
   * 특정 모임의 참여자 목록(ACCEPTED)
   */
  @GetMapping("/{clubId}/accepted-members")
  public ResponseEntity<Page<ReadingClubUserResponseDto>> getAcceptedMembers(
      @PathVariable Long clubId,
      Pageable pageable
  ) {
    Page<ReadingClubUserResponseDto> page = readingClubService.getReadingClubUsersByStatus(
        clubId, ReadingClubUserStatus.ACCEPTED, pageable);
    return ResponseEntity.ok(page);
  }

  /**
   * 특정 모임의 신청자 목록(PENDING)
   */
  @GetMapping("/{clubId}/pending-members")
  public ResponseEntity<Page<ReadingClubUserResponseDto>> getPendingMembers(
      @PathVariable Long clubId,
      Pageable pageable
  ) {
    Page<ReadingClubUserResponseDto> page = readingClubService.getReadingClubUsersByStatus(
        clubId, ReadingClubUserStatus.PENDING, pageable);
    return ResponseEntity.ok(page);
  }

  /**
   * [사용자] 독서모임 가입 신청
   */
  @PostMapping("/{clubId}/apply")
  public ResponseEntity<Void> applyForReadingClub(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long clubId
  ) {
    Long userId = userDetails.getUser().getId();
    readingClubJoinService.applyForReadingClub(userId, clubId);
    return ResponseEntity.ok().build();
  }

  /**
   * [모임장] 독서모임 가입 승인
   * @param targetUserId 승인 대상 사용자
   */
  @PatchMapping("/{clubId}/accept/{targetUserId}")
  public ResponseEntity<Void> acceptUser(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long clubId,
      @PathVariable Long targetUserId
  ) {
    Long hostUserId = userDetails.getUser().getId();
    readingClubJoinService.acceptUser(hostUserId, clubId, targetUserId);
    return ResponseEntity.ok().build();
  }

  /**
   * [모임장] 가입 거절
   * @param targetUserId 거절 대상 사용자
   */
  @PatchMapping("/{clubId}/reject/{targetUserId}")
  public ResponseEntity<Void> rejectUser(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long clubId,
      @PathVariable Long targetUserId
  ) {
    Long hostUserId = userDetails.getUser().getId();
    readingClubJoinService.rejectUser(hostUserId, clubId, targetUserId);
    return ResponseEntity.ok().build();
  }

  /**
   * [사용자] 독서모임 탈퇴
   */
  @DeleteMapping("/{clubId}/leave")
  public ResponseEntity<Void> leaveReadingClub(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long clubId
  ) {
    Long userId = userDetails.getUser().getId();
    readingClubJoinService.leaveReadingClub(userId, clubId);
    return ResponseEntity.ok().build();
  }
}
