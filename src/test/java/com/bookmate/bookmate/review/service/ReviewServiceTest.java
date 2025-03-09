package com.bookmate.bookmate.review.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bookmate.bookmate.review.dto.ReviewRequestDto;
import com.bookmate.bookmate.review.entity.Review;
import com.bookmate.bookmate.review.repository.ReviewRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

  @Mock
  ReviewRepository reviewRepository;

  @InjectMocks
  ReviewService reviewService;

  @Test
  @DisplayName("리뷰 생성 - 성공")
  void createReview_success() {
    //given
    ReviewRequestDto reviewDto = new ReviewRequestDto("title", "content", "isbn", 3);
    Review review = Review.builder().title(reviewDto.getTitle()).content(reviewDto.getContent())
        .isbn(reviewDto.getIsbn()).rating(reviewDto.getRating()).build();

    when(reviewRepository.save(any(Review.class))).thenReturn(review);

    //when
    reviewService.createReview(reviewDto);

    //then
    verify(reviewRepository).save(any(Review.class));
  }

  @Test
  @DisplayName("특정 리뷰 조회 - 성공")
  void getReview_success() {
    //given
    Review review = Review.builder().title("title").content("content").isbn("1111111111111")
        .rating(3).build();
    when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

    //when
    Review result = reviewService.getReview(1L);

    //then
    assertNotNull(result);
    assertEquals("title", result.getTitle());
    verify(reviewRepository).findById(1L);
  }

  @Test
  @DisplayName("특정 리뷰 조회 실패 - 리뷰 없음")
  void getReview_notFound() {
    //given
    when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

    //when
    Exception exception = assertThrows(RuntimeException.class, () -> reviewService.getReview(1L));
    //then
    assertEquals("리뷰가 없습니다.", exception.getMessage());
    verify(reviewRepository).findById(1L);
  }

  @Test
  @DisplayName("Isbn으로 리뷰 리스트 조회 - 성공")
  void getReviewsByIsbn_success() {
    //given
    String isbn = "1234567891011";
    List<Review> reviews = Arrays.asList(new Review(), new Review());
    when(reviewRepository.findAllByIsbn(isbn)).thenReturn(reviews);

    //when
    List<Review> result = reviewService.getReviewsByIsbn(isbn);

    //then
    assertNotNull(result);
    assertEquals(2, reviews.size());
    verify(reviewRepository).findAllByIsbn(isbn);
  }

  @Test
  @DisplayName("리뷰 업데이트 - 성공")
  void updateReview_success() {
    //given
    Review review = Review.builder().content("title").content("content").rating(5)
        .isbn("1111111111111").build();
    ReviewRequestDto reviewDto = new ReviewRequestDto("new_title", "new_content", "1111111111111", 3);
    when(reviewRepository.findById(1L)).thenReturn(Optional.of((review)));

    //when
    reviewService.updateReview(1L, reviewDto);

    //then
    assertEquals(review.getTitle(), reviewDto.getTitle());
    assertEquals(review.getContent(), reviewDto.getContent());
    assertEquals(review.getRating(), reviewDto.getRating());
    assertEquals(review.getIsbn(), reviewDto.getIsbn());
    verify(reviewRepository).findById(1L);
  }

  @Test
  @DisplayName("리뷰 삭제 - 성공")
  void deleteReview_success() {
    //given
    Review review = Review.builder().title("title").content("content").isbn("1111111111111")
        .rating(3).build();
    when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

    //when
    reviewService.deleteReview(1L);
    //then
    assertNotNull(review.getDeletedAt());
    verify(reviewRepository).findById(1L);
  }
}