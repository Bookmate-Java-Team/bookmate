package com.bookmate.bookmate.like.exception;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.common.error.exception.NotFoundException;

public class LikeNotFoundException extends NotFoundException {


  public LikeNotFoundException(String value) {
    super(value, ErrorCode.LIKE_NOT_FOUND);
  }
}
