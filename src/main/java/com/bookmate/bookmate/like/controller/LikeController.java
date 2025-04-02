package com.bookmate.bookmate.like.controller;

import com.bookmate.bookmate.common.security.CustomUserDetails;
import com.bookmate.bookmate.like.entity.enums.TargetType;
import com.bookmate.bookmate.like.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
@Tag(name = "Like API", description = "좋아요 관련 API")
public class LikeController {

  private final LikeService likeService;

  @PostMapping("/{targetType}/{targetId}/toggle")
  @Operation(summary = "좋아요 토글", description = "좋아요를 추가하거나 제거합니다.")
  @ApiResponse(responseCode = "200", description = "좋아요 상태 변경 성공")
  public ResponseEntity<String> toggleLike(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @Parameter(description = "좋아요 대상 타입 (POST 또는 COMMENT)", example = "POST") @PathVariable TargetType targetType,
      @Parameter(description = "좋아요 대상 ID", example = "1") @PathVariable Long targetId
  ) {
    Long userId = customUserDetails.getUser().getId();
    return ResponseEntity.ok(likeService.toggleLike(userId, targetId, targetType));
  }

  @GetMapping("/{targetType}/{targetId}/count")
  @Operation(summary = "좋아요 개수 조회", description = "특정 대상의 좋아요 개수를 반환합니다.")
  @ApiResponse(responseCode = "200", description = "좋아요 개수 반환 성공")
  public ResponseEntity<Integer> getLikeCount(
      @Parameter(description = "좋아요 대상 타입 (POST 또는 COMMENT)", example = "POST") @PathVariable TargetType targetType,
      @Parameter(description = "좋아요 대상 ID", example = "1") @PathVariable Long targetId
  ) {
    return ResponseEntity.ok(likeService.getLikeCount(targetId, targetType));
  }

}
