package com.bookmate.bookmate.review.controller;

import com.bookmate.bookmate.review.dto.ReviewDto;
import com.bookmate.bookmate.review.service.ReviewService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
@ResponseBody
public class ReviewController {
  private final ReviewService reviewService;

  @PostMapping
  public ResponseEntity<String> createReview(@Valid ReviewDto reviewDto) {
    reviewService.createReview(reviewDto);
    return ResponseEntity.ok("리뷰가 작성되었습니다.");
  }

  @GetMapping("/{id}")
  public ResponseEntity<ReviewDto> getReview(@PathVariable Long id) {
    return ResponseEntity.ok(ReviewDto.toDto(reviewService.getReview(id)));
  }

  @GetMapping("/{isbn}/list.do")
  public ResponseEntity<List<ReviewDto>> getReviewsByIsbn(@PathVariable String isbn) {
    return ResponseEntity.ok(reviewService.getReviewsByIsbn(isbn).stream().map(ReviewDto::toDto).toList());
  }

  @PutMapping("/{id}")
  public ResponseEntity<String> updateReview(@PathVariable Long id, @Valid ReviewDto reviewDto) {
    reviewService.updateReview(id, reviewDto);
    return ResponseEntity.ok("리뷰가 수정되었습니다.");
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteReview(@PathVariable Long id) {
    reviewService.deleteReview(id);
    return ResponseEntity.ok("리뷰가 삭제되었습니다.");
  }


}
