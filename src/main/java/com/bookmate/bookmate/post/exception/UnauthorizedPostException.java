package com.bookmate.bookmate.post.exception;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.common.error.exception.UnauthorizedException;

public class UnauthorizedPostException extends UnauthorizedException {

  public UnauthorizedPostException(ErrorCode errorCode) {
    super(errorCode);
  }
}
