package com.bookmate.bookmate.review.service;

import com.bookmate.bookmate.book.entity.Book;
import com.bookmate.bookmate.book.entity.UserBookRecord;
import com.bookmate.bookmate.book.exception.BookNotFoundException;
import com.bookmate.bookmate.book.repository.BookRepository;
import com.bookmate.bookmate.book.repository.UserBookRecordRepository;
import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.review.dto.ReviewRequestDto;
import com.bookmate.bookmate.review.dto.ReviewUpdateRequestDto;
import com.bookmate.bookmate.review.entity.Review;
import com.bookmate.bookmate.review.exception.ReviewDuplicateException;
import com.bookmate.bookmate.review.exception.ReviewForbiddenException;
import com.bookmate.bookmate.review.exception.ReviewNotFoundException;
import com.bookmate.bookmate.review.repository.ReviewRepository;
import com.bookmate.bookmate.user.entity.User;
import com.bookmate.bookmate.user.exception.UserNotFoundException;
import com.bookmate.bookmate.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final UserRepository userRepository;
  private final UserBookRecordRepository userBookRecordRepository;
  private final BookRepository bookRepository;

  @Transactional
  public Review createReview(Long userId, Long bookId, ReviewRequestDto reviewRequestDto) {
    User user = findUserById(userId);

    Book book = findBookById(bookId);

    UserBookRecord userBookRecord = findUserBookRecordByUserAndBook(user, book);

    return reviewRepository.save(
        reviewRequestDto.toEntity(userBookRecord));
  }

  @Transactional(readOnly = true)
  public Review getReview(Long id) {
    return findReviewById(id);
  }

  @Transactional(readOnly = true)
  public Page<Review> getReviews(Long bookId, Pageable pageable) {
    Book book = findBookById(bookId);

    Page<UserBookRecord> userBookRecords = findUserBookRecordsByBook(book, pageable);

    List<Review> reviews = userBookRecords.stream().map(this::findReviewByUserBookRecord)
        .collect(Collectors.toList());

    return new PageImpl<>(reviews, pageable, userBookRecords.getTotalElements());
  }

  @Transactional
  public Review updateReview(Long userId, Long id, ReviewUpdateRequestDto reviewUpdateRequestDto) {
    User user = findUserById(userId);
    Review review = findReviewById(id);

    if (review.getUserBookRecord().getUser() != user) {
      throw new ReviewForbiddenException(ErrorCode.REVIEW_UPDATE_DENIED);
    }

    return review.update(reviewUpdateRequestDto);
  }

  @Transactional
  public void deleteReview(Long userId, Long id) {
    User user = findUserById(userId);
    Review review = findReviewById(id);

    if (review.getUserBookRecord().getUser() != user) {
      throw new ReviewForbiddenException(ErrorCode.REVIEW_DELETE_DENIED);
    }

    review.softDelete();
  }

  private Review findReviewById(Long id) {
    return reviewRepository.findById(id).orElseThrow(() -> new ReviewNotFoundException(id));
  }

  private Review findReviewByUserBookRecord(UserBookRecord userBookRecord) {
    return reviewRepository.findByUserBookRecord(userBookRecord)
        .orElseThrow(() -> new ReviewNotFoundException(userBookRecord.getId()));
  }

  private User findUserById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  private Book findBookById(Long id) {
    return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
  }

  private Page<UserBookRecord> findUserBookRecordsByBook(Book book, Pageable pageable) {
    return userBookRecordRepository.findAllByBook(book, pageable)
        .orElseThrow(() -> new ReviewNotFoundException(book.getIsbn()));
  }

  private UserBookRecord findUserBookRecordByUserAndBook(User user, Book book) {
    return userBookRecordRepository.findByUserAndBook(user, book)
        .orElseThrow(() -> new ReviewDuplicateException(user.getNickname() + " " + book.getIsbn()));
  }


}
