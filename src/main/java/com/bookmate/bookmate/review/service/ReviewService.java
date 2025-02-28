package com.bookmate.bookmate.review.service;

import com.bookmate.bookmate.review.dto.ReviewDto;
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
  public void createReview(ReviewDto reviewDto) {
    reviewRepository.save(
        Review.builder().isbn(reviewDto.getIsbn()).title(reviewDto.getTitle())
            .content(reviewDto.getContent()).rating(reviewDto.getRating()).build());
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
  public void updateReview(Long id, @Valid ReviewDto reviewDto) {
    Review review = findReviewById(id);

    review.update(reviewDto);
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
