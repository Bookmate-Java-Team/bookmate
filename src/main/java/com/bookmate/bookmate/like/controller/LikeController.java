package com.bookmate.bookmate.like.controller;

import com.bookmate.bookmate.common.security.CustomUserDetails;
import com.bookmate.bookmate.like.dto.LikeRequestDto;
import com.bookmate.bookmate.like.dto.LikeResponseDto;
import com.bookmate.bookmate.like.entity.enums.TargetType;
import com.bookmate.bookmate.like.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {

  private final LikeService likeService;

  @PostMapping("/toggle")
  public ResponseEntity<String> toggleLike(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @Valid LikeRequestDto likeRequestDto) {
    Long userId = customUserDetails.getUser().getId();
    return ResponseEntity.ok(likeService.toggleLike(userId, likeRequestDto));
  }

  @GetMapping("/count")
  public ResponseEntity<Integer> getLikeCount(@RequestParam Long targetId, @RequestParam
  TargetType targetType) {
    return ResponseEntity.ok(likeService.getLikeCount(targetId, targetType));
  }

}
