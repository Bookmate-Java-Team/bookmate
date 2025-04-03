package com.bookmate.bookmate.readingclub.controller;

import com.bookmate.bookmate.common.security.CustomUserDetails;
import com.bookmate.bookmate.readingclub.dto.request.ReadingClubRequestDto;
import com.bookmate.bookmate.readingclub.dto.response.ReadingClubResponseDto;
import com.bookmate.bookmate.readingclub.dto.response.ReadingClubUserResponseDto;
import com.bookmate.bookmate.readingclub.entity.enums.ReadingClubUserStatus;
import com.bookmate.bookmate.readingclub.service.ReadingClubJoinService;
import com.bookmate.bookmate.readingclub.service.ReadingClubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Reading Club API", description = "독서 모임 관련 API")
public class ReadingClubController {

  private final ReadingClubService readingClubService;
  private final ReadingClubJoinService readingClubJoinService;

  /**
   * 독서모임 생성
   */
  @PostMapping
  @Operation(summary = "독서 모임 생성", description = "독서 모임을 생성합니다.")
  @ApiResponse(responseCode = "200", description = "독서 모임 생성 성공",
  content = @Content(schema = @Schema(implementation = ReadingClubResponseDto.class)))
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
  @Operation(summary = "독서 모임 수정", description = "독서 모임을 수정합니다..")
  @ApiResponse(responseCode = "200", description = "독서 모임 수정 성공",
      content = @Content(schema = @Schema(implementation = ReadingClubResponseDto.class)))
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
  @Operation(summary = "독서 모임 삭제", description = "독서 모임을 삭제합니다.")
  @ApiResponse(responseCode = "200", description = "독서 모임 삭제 성공")
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
  @Operation(summary = "특정 회원이 가입한 독서 모임 조회", description = "특정 회원이 가입한 독서모임을 조회합니다.")
  @ApiResponse(responseCode = "200", description = "독서 모임 조회 성공")
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
  @Operation(summary = "전체 독서 모임 조회", description = "전체 독서 모임들을 조회합니다..")
  @ApiResponse(responseCode = "200", description = "독서 모임 조회 성공")
  public ResponseEntity<Page<ReadingClubResponseDto>> getAllReadingClubs(Pageable pageable) {
    return ResponseEntity.ok(readingClubService.getAllReadingClubs(pageable));
  }

  /**
   * 특정 독서모임 조회
   */
  @GetMapping("/{clubId}")
  @Operation(summary = "특정 독서 모임 조회", description = "특정 독서 모임을 조회합니다.")
  @ApiResponse(responseCode = "200", description = "독서 모임 생성 성공",
      content = @Content(schema = @Schema(implementation = ReadingClubResponseDto.class)))
  public ResponseEntity<ReadingClubResponseDto> getReadingClub(@PathVariable Long clubId) {
    return ResponseEntity.ok(readingClubService.getReadingClub(clubId));
  }

  /**
   * 특정 모임의 참여자 목록(ACCEPTED)
   */
  @GetMapping("/{clubId}/accepted-members")
  @Operation(summary = "특정 모임의 참여자 목록 조회", description = "특정 독서 모임의 참여자 목록을 조회합니다.")
  @ApiResponse(responseCode = "200", description = "참여자 목록 조회 성공")
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
  @Operation(summary = "특정 모임의 신청자 목록", description = "특정 독서 모임의 신청자 목록을 조회합니다.")
  @ApiResponse(responseCode = "200", description = "신청자 목록 조회 성공")
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
  @Operation(summary = "독서 모임 가입 신청", description = "독서 모임에 가입을 신청합니다.")
  @ApiResponse(responseCode = "200", description = "독서 모임 가입 신청 성공")
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
  @Operation(summary = "독서 모임 가입 승인", description = "독서 모임의 가입을 승인합니다.")
  @ApiResponse(responseCode = "200", description = "가입 승인 성공")
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
  @Operation(summary = "독서 모임 가입 거절", description = "독서 모임 가입을 거절합니다.")
  @ApiResponse(responseCode = "200", description = "독서 모임 가입 거절 성공")
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
  @Operation(summary = "독서 모임 탈퇴", description = "독서 모임을 탈퇴합니다.")
  @ApiResponse(responseCode = "200", description = "독서 모임 탈퇴 성공")
  public ResponseEntity<Void> leaveReadingClub(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long clubId
  ) {
    Long userId = userDetails.getUser().getId();
    readingClubJoinService.leaveReadingClub(userId, clubId);
    return ResponseEntity.ok().build();
  }
}
