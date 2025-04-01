package com.bookmate.bookmate.like.controller;

import com.bookmate.bookmate.common.security.CustomUserDetails;
import com.bookmate.bookmate.like.entity.enums.TargetType;
import com.bookmate.bookmate.like.service.LikeService;
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
public class LikeController {

  private final LikeService likeService;

  @PostMapping("/{targetType}/{targetId}/toggle")
  public ResponseEntity<String> toggleLike(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @PathVariable TargetType targetType,
      @PathVariable Long targetId
  ) {
    Long userId = customUserDetails.getUser().getId();
    return ResponseEntity.ok(likeService.toggleLike(userId, targetId, targetType));
  }

  @GetMapping("/{targetType}/{targetId}/count")
  public ResponseEntity<Integer> getLikeCount(
      @PathVariable TargetType targetType,
      @PathVariable Long targetId
  ) {
    return ResponseEntity.ok(likeService.getLikeCount(targetId, targetType));
  }

}
