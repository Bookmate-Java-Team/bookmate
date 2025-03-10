package com.bookmate.bookmate.review.exception;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.common.error.exception.ForbiddenException;

public class ReviewForbiddenException extends ForbiddenException {

  public ReviewForbiddenException(final ErrorCode errorCode) {
    super(errorCode);
  }
}
