package com.bookmate.bookmate.comment.controller;

import com.bookmate.bookmate.comment.dto.CommentRequestDto;
import com.bookmate.bookmate.comment.dto.CommentResponseDto;
import com.bookmate.bookmate.comment.exception.CommentDepthLimitExceededException;
import com.bookmate.bookmate.comment.exception.CommentForbiddenException;
import com.bookmate.bookmate.comment.service.CommentService;
import com.bookmate.bookmate.common.security.CustomUserDetails;
import com.bookmate.bookmate.post.dto.PostResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Comment API", description = "댓글, 대댓글 관련 API")
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/posts/{postId}/comments")
  @Operation(summary = "특정 post의 comment 생성", description = "특정 게시글의 댓글과 대댓글 작성 가능")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "댓글이 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = CommentResponseDto.class))),
      @ApiResponse(responseCode = "COMMENT-002", description = "Comment Depth Limit Exceeded",
          content = @Content(schema = @Schema(implementation = CommentDepthLimitExceededException.class)))
  })
  public ResponseEntity<CommentResponseDto> addComment(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @PathVariable Long postId,
      @Valid CommentRequestDto commentRequestDto) {
    Long userId = customUserDetails.getUser().getId();
    return ResponseEntity.status(
        HttpStatus.CREATED).body(
        CommentResponseDto.toDto(commentService.addComment(userId, postId, commentRequestDto)));
  }

  @PatchMapping("/comments/{commentId}")
  @Operation(summary = "comment 수정", description = "특정 댓글 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "댓글이 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = CommentResponseDto.class))),
      @ApiResponse(responseCode = "COMMENT-001", description = "Comment Not Found",
          content = @Content(schema = @Schema(implementation = CommentForbiddenException.class))),
      @ApiResponse(responseCode = "COMMENT-004", description = "You do not have permission to update this comment",
          content = @Content(schema = @Schema(implementation = CommentForbiddenException.class)))
  })
  public ResponseEntity<CommentResponseDto> updateComment(
      @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long commentId,
      @Valid CommentRequestDto commentRequestDto) {
    Long userId = customUserDetails.getUser().getId();
    return ResponseEntity.ok(CommentResponseDto.toDto(
        commentService.updateComment(userId, commentId, commentRequestDto)));
  }

  @DeleteMapping("/comments/{commentId}")
  @Operation(summary = "comment 삭제", description = "특정 댓글 삭제")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "댓글이 성공적으로 삭제됨"),
      @ApiResponse(responseCode = "COMMENT-001", description = "Comment Not Found",
          content = @Content(schema = @Schema(implementation = CommentForbiddenException.class))),
      @ApiResponse(responseCode = "COMMENT-004", description = "You do not have permission to delete this comment",
          content = @Content(schema = @Schema(implementation = CommentForbiddenException.class)))
  })
  public ResponseEntity<String> deleteComment(
      @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long commentId) {
    Long userId = customUserDetails.getUser().getId();
    commentService.deleteComment(userId, commentId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/posts/{postId}/comments")
  @Operation(summary = "특정 post의 comments 조회", description = "특정 게시글의 댓글들을 Pagenation으로 조회")
  @ApiResponse(responseCode = "200", description = "댓글 조회 성공")
  public ResponseEntity<Page<CommentResponseDto>> getComments(
      @PathVariable Long postId,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size
  ) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());

    return ResponseEntity.ok(commentService.getComments(postId, pageable));
  }
}
