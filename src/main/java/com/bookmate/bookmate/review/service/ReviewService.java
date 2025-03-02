package com.bookmate.bookmate.review.service;

import com.bookmate.bookmate.review.dto.ReviewRequestDto;
import com.bookmate.bookmate.review.entity.Review;
import com.bookmate.bookmate.review.repository.ReviewRepository;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;

  @Transactional
  public Review createReview(ReviewRequestDto reviewRequestDto) {
    return reviewRepository.save(
        reviewRequestDto.toEntity());
  }

  @Transactional(readOnly = true)
  public Review getReview(Long id) {
    return findReviewById(id);
  }

  @Transactional(readOnly = true)
  public List<Review> getReviewsByIsbn(String isbn) {
    return reviewRepository.findAllByIsbn(isbn);
  }

  @Transactional
  public Review updateReview(Long id, @Valid ReviewRequestDto reviewRequestDto) {
    Review review = findReviewById(id);

    return review.update(reviewRequestDto);
  }

  @Transactional
  public void deleteReview(Long id) {
    Review review = findReviewById(id);
    review.softDelete();
  }

  private Review findReviewById(Long id) {
    return reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("리뷰가 없습니다."));
  }
}
