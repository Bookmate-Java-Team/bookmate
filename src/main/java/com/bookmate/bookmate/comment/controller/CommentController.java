package com.bookmate.bookmate.comment.controller;

import com.bookmate.bookmate.comment.dto.CommentRequestDto;
import com.bookmate.bookmate.comment.dto.CommentResponseDto;
import com.bookmate.bookmate.comment.service.CommentService;
import com.bookmate.bookmate.common.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/posts/{postId}/comments")
  public ResponseEntity<CommentResponseDto> addComment(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @PathVariable Long postId,
      @Valid CommentRequestDto commentRequestDto) {
    Long userId = customUserDetails.getUser().getId();
    return ResponseEntity.status(
        HttpStatus.CREATED).body(
        CommentResponseDto.toDto(commentService.addComment(userId, postId, commentRequestDto)));
  }

  @PatchMapping("comments/{commentId}")
  public ResponseEntity<CommentResponseDto> updateComment(
      @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long commentId,
      @Valid CommentRequestDto commentRequestDto) {
    Long userId = customUserDetails.getUser().getId();
    return ResponseEntity.ok(CommentResponseDto.toDto(
        commentService.updateComment(userId, commentId, commentRequestDto)));
  }

  @DeleteMapping("comments/{commentId}")
  public ResponseEntity<String> deleteComment(
      @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long commentId) {
    Long userId = customUserDetails.getUser().getId();
    commentService.deleteComment(userId, commentId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/posts/{postId}/comments")
  public ResponseEntity<Page<CommentResponseDto>> getComments(
      @PathVariable Long postId,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());

    return ResponseEntity.ok(commentService.getComments(postId, pageable));
  }
}
