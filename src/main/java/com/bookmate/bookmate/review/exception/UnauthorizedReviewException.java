package com.bookmate.bookmate.review.exception;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.common.error.exception.UnauthorizedException;

public class UnauthorizedReviewException extends UnauthorizedException {

  public UnauthorizedReviewException(ErrorCode errorCode) {
    super(errorCode);
  }
}
