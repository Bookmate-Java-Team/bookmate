package com.bookmate.bookmate.post.controller;

import com.bookmate.bookmate.common.security.CustomUserDetails;
import com.bookmate.bookmate.post.dto.PostRequestDto;
import com.bookmate.bookmate.post.dto.PostResponseDto;
import com.bookmate.bookmate.post.dto.PostUpdateRequestDto;
import com.bookmate.bookmate.post.service.PostService;
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
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @PostMapping
  public ResponseEntity<PostResponseDto> createPost(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Valid PostRequestDto postRequestDto) {
    Long userId = userDetails.getUser().getId();
    return ResponseEntity.status(
            HttpStatus.CREATED)
        .body(PostResponseDto.toDto(postService.createPost(userId, postRequestDto)));
  }

  @PatchMapping("/{postId}")
  public ResponseEntity<PostResponseDto> updatePost(
      @PathVariable Long postId,
      @AuthenticationPrincipal CustomUserDetails userDetails,
      PostUpdateRequestDto postUpdateRequestDto
  ) {
    Long userId = userDetails.getUser().getId();
    return ResponseEntity.ok(
        PostResponseDto.toDto(postService.updatePost(userId, postId, postUpdateRequestDto)));
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<String> deletePost(
      @PathVariable Long postId,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    Long userId = userDetails.getUser().getId();
    postService.deletePost(userId, postId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{postId}")
  public ResponseEntity<PostResponseDto> getPost(
      @PathVariable Long postId
  ) {
    return ResponseEntity.ok(PostResponseDto.toDto(postService.getPost(postId)));
  }

  @GetMapping
  public ResponseEntity<Page<PostResponseDto>> getAllPosts(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

    return ResponseEntity.ok(postService.getAllPosts(pageable).map(PostResponseDto::toDto));
  }
}
