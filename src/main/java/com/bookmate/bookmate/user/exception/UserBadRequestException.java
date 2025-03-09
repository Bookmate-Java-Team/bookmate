package com.bookmate.bookmate.user.exception;

import com.bookmate.bookmate.common.error.exception.BusinessException;
import com.bookmate.bookmate.common.error.exception.ErrorCode;

public class UserBadRequestException extends BusinessException {

  public UserBadRequestException(ErrorCode errorCode) {
    super(errorCode);
  }
}
