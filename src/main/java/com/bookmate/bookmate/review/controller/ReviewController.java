package com.bookmate.bookmate.review.controller;

import com.bookmate.bookmate.common.security.CustomUserDetails;
import com.bookmate.bookmate.review.dto.ReviewRequestDto;
import com.bookmate.bookmate.review.dto.ReviewResponseDto;
import com.bookmate.bookmate.review.dto.ReviewUpdateRequestDto;
import com.bookmate.bookmate.review.service.ReviewService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
@ResponseBody
public class ReviewController {

  private final ReviewService reviewService;

  @PostMapping
  public ResponseEntity<ReviewResponseDto> createReview(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid ReviewRequestDto reviewRequestDto) {
    Long userId = userDetails.getUser().getId();
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ReviewResponseDto.toDto(reviewService.createReview(userId, reviewRequestDto)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ReviewResponseDto> getReview(@PathVariable Long id) {
    return ResponseEntity.ok(ReviewResponseDto.toDto(reviewService.getReview(id)));
  }

  @GetMapping("/{isbn}/list.do")
  public ResponseEntity<List<ReviewResponseDto>> getReviewsByIsbn(@PathVariable String isbn) {
    return ResponseEntity.ok(
        reviewService.getReviewsByIsbn(isbn).stream().map(ReviewResponseDto::toDto).toList());
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ReviewResponseDto> updateReview(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id,
      @Valid ReviewUpdateRequestDto reviewUpdateRequestDto) {
    Long userId = userDetails.getUser().getId();
    return ResponseEntity.ok(
        ReviewResponseDto.toDto(reviewService.updateReview(userId, id, reviewUpdateRequestDto)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteReview(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id) {
    Long userId = userDetails.getUser().getId();
    reviewService.deleteReview(userId, id);
    return ResponseEntity.noContent().build();
  }


}
