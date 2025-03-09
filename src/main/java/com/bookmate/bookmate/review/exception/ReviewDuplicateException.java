package com.bookmate.bookmate.review.exception;

import com.bookmate.bookmate.common.error.exception.DuplicateException;
import com.bookmate.bookmate.common.error.exception.ErrorCode;

public class ReviewDuplicateException extends DuplicateException {

  public ReviewDuplicateException(String value) {
    super(value, ErrorCode.DUPLICATE_REVIEW);
  }
}
