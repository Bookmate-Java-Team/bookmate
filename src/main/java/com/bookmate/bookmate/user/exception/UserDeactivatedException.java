package com.bookmate.bookmate.user.exception;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.common.error.exception.ForbiddenException;

public class UserDeactivatedException extends ForbiddenException {

  public UserDeactivatedException() {
    super(ErrorCode.USER_DEACTIVATED);
  }
}
