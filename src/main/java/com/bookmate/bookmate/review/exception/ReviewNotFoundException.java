package com.bookmate.bookmate.review.exception;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.common.error.exception.NotFoundException;

public class ReviewNotFoundException extends NotFoundException {

  public ReviewNotFoundException(Long value) {
    super(value, ErrorCode.REVIEW_NOT_FOUND);
  }

  public ReviewNotFoundException(String value) {
    super(value, ErrorCode.REVIEW_NOT_FOUND);

  }
}
