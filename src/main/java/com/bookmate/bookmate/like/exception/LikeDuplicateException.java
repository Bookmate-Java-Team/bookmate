package com.bookmate.bookmate.like.exception;

import com.bookmate.bookmate.common.error.exception.DuplicateException;
import com.bookmate.bookmate.common.error.exception.ErrorCode;

public class LikeDuplicateException extends DuplicateException {


  public LikeDuplicateException(String value) {
    super(value, ErrorCode.DUPLICATE_LIKE);
  }
}
