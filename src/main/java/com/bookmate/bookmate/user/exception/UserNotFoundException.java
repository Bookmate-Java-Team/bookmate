package com.bookmate.bookmate.user.exception;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.common.error.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

  public UserNotFoundException(final Long userId) {
    super(userId, ErrorCode.USER_NOT_FOUND);
  }

  public UserNotFoundException(final String userInfo) {
    super(userInfo, ErrorCode.USER_NOT_FOUND);
  }
}
