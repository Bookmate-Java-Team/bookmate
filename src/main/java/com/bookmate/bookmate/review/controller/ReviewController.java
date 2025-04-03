package com.bookmate.bookmate.review.controller;

import com.bookmate.bookmate.common.security.CustomUserDetails;
import com.bookmate.bookmate.review.dto.ReviewRequestDto;
import com.bookmate.bookmate.review.dto.ReviewResponseDto;
import com.bookmate.bookmate.review.dto.ReviewUpdateRequestDto;
import com.bookmate.bookmate.review.exception.ReviewForbiddenException;
import com.bookmate.bookmate.review.exception.ReviewNotFoundException;
import com.bookmate.bookmate.review.service.ReviewService;
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
@Tag(name = "Review API", description = "도서 리뷰 관련 API")
public class ReviewController {

  private final ReviewService reviewService;

  @PostMapping("/books/{bookId}/reviews")
  @Operation(summary = "책의 review 생성", description = "책에 대한 리뷰를 작성합니다.")
  @ApiResponse(responseCode = "201", description = "리뷰가 성공적으로 생성됨",
      content = @Content(schema = @Schema(implementation = ReviewResponseDto.class)))
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
  @Operation(summary = "특정 review 조회", description = "특정 리뷰를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "리뷰가 성공적으로 조회됨",
      content = @Content(schema = @Schema(implementation = ReviewResponseDto.class)))
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
  @Operation(summary = "특정 책의 reviews 조회", description = "특정 책에 대한 리뷰를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "리뷰가 성공적으로 조회됨")
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
  @Operation(summary = "review 수정", description = "특정 리뷰를 수정합니다..")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "리뷰가 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = ReviewResponseDto.class))),
      @ApiResponse(responseCode = "R-004", description = "You do not have permission to update this review", content = @Content(schema = @Schema(implementation = ReviewForbiddenException.class))),
      @ApiResponse(responseCode = "R-001", description = "Review Not Found", content = @Content(schema = @Schema(implementation = ReviewNotFoundException.class)))
  })
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
  @Operation(summary = "review 삭제", description = "특정 리뷰를 삭제합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "리뷰 삭제 성공"),
      @ApiResponse(responseCode = "R-005", description = "You do not have permission to delete this review", content = @Content(schema = @Schema(implementation = ReviewForbiddenException.class))),
      @ApiResponse(responseCode = "R-001", description = "Review Not Found", content = @Content(schema = @Schema(implementation = ReviewNotFoundException.class)))
  })
  public ResponseEntity<String> deleteReview(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long reviewId) {
    Long userId = userDetails.getUser().getId();
    reviewService.deleteReview(userId, reviewId);
    return ResponseEntity.noContent().build();
  }


}
