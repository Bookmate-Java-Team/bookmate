package com.bookmate.bookmate.review.controller;

import com.bookmate.bookmate.common.security.CustomUserDetails;
import com.bookmate.bookmate.review.dto.ReviewRequestDto;
import com.bookmate.bookmate.review.dto.ReviewResponseDto;
import com.bookmate.bookmate.review.dto.ReviewUpdateRequestDto;
import com.bookmate.bookmate.review.service.ReviewService;
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
public class ReviewController {

  private final ReviewService reviewService;


  @PostMapping("/books/{bookId}/reviews")
  public ResponseEntity<ReviewResponseDto> createReview(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long bookId,
      @Valid ReviewRequestDto reviewRequestDto) {
    Long userId = userDetails.getUser().getId();
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            ReviewResponseDto.toDto(reviewService.createReview(userId, bookId, reviewRequestDto)));
  }

  @GetMapping("reviews/{reviewId}")
  public ResponseEntity<ReviewResponseDto> getReview(@PathVariable Long reviewId) {
    return ResponseEntity.ok(ReviewResponseDto.toDto(reviewService.getReview(reviewId)));
  }

  /**
   * 특정 책 리뷰들을 조회합니다.
   *
   * @param bookId
   * @return 특정 책 리뷰들 ({@link ReviewResponseDto} 리스트)
   */
  @GetMapping("/books/{bookId}/reviews")
  public ResponseEntity<Page<ReviewResponseDto>> getReviews(
      @PathVariable Long bookId,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

    return ResponseEntity.ok(
        reviewService.getReviews(bookId, pageable).map(ReviewResponseDto::toDto));
  }

  @PatchMapping("reviews/{reviewId}")
  public ResponseEntity<ReviewResponseDto> updateReview(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long reviewId,
      @Valid ReviewUpdateRequestDto reviewUpdateRequestDto) {
    Long userId = userDetails.getUser().getId();
    return ResponseEntity.ok(
        ReviewResponseDto.toDto(
            reviewService.updateReview(userId, reviewId, reviewUpdateRequestDto)));
  }

  @DeleteMapping("reviews/{reviewId}")
  public ResponseEntity<String> deleteReview(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long reviewId) {
    Long userId = userDetails.getUser().getId();
    reviewService.deleteReview(userId, reviewId);
    return ResponseEntity.noContent().build();
  }


}
