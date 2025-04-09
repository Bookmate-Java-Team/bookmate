package com.bookmate.bookmate.post.controller;

import com.bookmate.bookmate.common.security.CustomUserDetails;
import com.bookmate.bookmate.post.dto.PostRequestDto;
import com.bookmate.bookmate.post.dto.PostResponseDto;
import com.bookmate.bookmate.post.dto.PostUpdateRequestDto;
import com.bookmate.bookmate.post.entity.enums.PostCategory;
import com.bookmate.bookmate.post.exception.PostForbiddenException;
import com.bookmate.bookmate.post.exception.PostNotFoundException;
import com.bookmate.bookmate.post.service.PostService;
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
@Tag(name = "Post API", description = "게시글 관련 API")
public class PostController {

  private final PostService postService;

  @PostMapping("/posts")
  @Operation(summary = "Post 생성", description = "카테고리에 따라 게시글을 생성합니다.\n"
      + "Category: FreeBookReview, ReadingClubRecruitment, FreeBoard")
  @ApiResponse(responseCode = "201", description = "게시글이 성공적으로 생성됨",
      content = @Content(schema = @Schema(implementation = PostResponseDto.class)))
  public ResponseEntity<PostResponseDto> createPost(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Valid PostRequestDto postRequestDto) {
    Long userId = userDetails.getUser().getId();
    return ResponseEntity.status(
            HttpStatus.CREATED)
        .body(PostResponseDto.toDto(postService.createPost(userId, postRequestDto)));
  }

  @PatchMapping("/posts/{postId}")
  @Operation(summary = "Post 수정", description = "게시글을 수정합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "게시글이 성공적으로 수정됨",
      content = @Content(schema = @Schema(implementation = PostResponseDto.class))),
      @ApiResponse(responseCode = "P-001", description = "Post Not Found",
          content = @Content(schema = @Schema(implementation = PostNotFoundException.class))),
      @ApiResponse(responseCode = "P-002", description = "You do not have permission to update this post",
      content = @Content(schema = @Schema(implementation = PostForbiddenException.class))),
  })
  public ResponseEntity<PostResponseDto> updatePost(
      @PathVariable Long postId,
      @AuthenticationPrincipal CustomUserDetails userDetails,
      PostUpdateRequestDto postUpdateRequestDto
  ) {
    Long userId = userDetails.getUser().getId();
    return ResponseEntity.ok(
        PostResponseDto.toDto(postService.updatePost(userId, postId, postUpdateRequestDto)));
  }

  @DeleteMapping("/posts/{postId}")
  @Operation(summary = "Post 삭제", description = "게시글을 삭제합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "게시글이 성공적으로 삭제됨",
          content = @Content(schema = @Schema(implementation = PostResponseDto.class))),
      @ApiResponse(responseCode = "P-001", description = "Post Not Found",
          content = @Content(schema = @Schema(implementation = PostNotFoundException.class))),
      @ApiResponse(responseCode = "P-003", description = "You do not have permission to delete this post",
          content = @Content(schema = @Schema(implementation = PostForbiddenException.class))),
  })
  public ResponseEntity<String> deletePost(
      @PathVariable Long postId,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    Long userId = userDetails.getUser().getId();
    postService.deletePost(userId, postId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/posts/{postId}")
  @Operation(summary = "특정 Post 조회", description = "특정 게시글을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "게시글이 성공적으로 조회됨",
          content = @Content(schema = @Schema(implementation = PostResponseDto.class))),
      @ApiResponse(responseCode = "P-001", description = "Post Not Found",
          content = @Content(schema = @Schema(implementation = PostNotFoundException.class))),
  })
  public ResponseEntity<PostResponseDto> getPost(
      @PathVariable Long postId
  ) {
    return ResponseEntity.ok(PostResponseDto.toDto(postService.getPost(postId)));
  }

  @GetMapping("/categories/{category}/posts")
  @Operation(summary = "특정 Category의 Posts 조회", description = "특정 카테고리의 게시글들을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "특정 카테고리의 게시글들이 조회됨")
  })
  public ResponseEntity<Page<PostResponseDto>> getAllPosts(
      @PathVariable(value = "category") PostCategory category,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size,
      @RequestParam(value = "sort", defaultValue = "createdAt") String sortBy
  ) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

    return ResponseEntity.ok(
        postService.getAllPosts(category, pageable).map(PostResponseDto::toDto));
  }
}
