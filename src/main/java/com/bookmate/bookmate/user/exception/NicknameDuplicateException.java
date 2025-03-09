package com.bookmate.bookmate.user.exception;

import com.bookmate.bookmate.common.error.exception.DuplicateException;
import com.bookmate.bookmate.common.error.exception.ErrorCode;

public class NicknameDuplicateException extends DuplicateException {

  public NicknameDuplicateException(final String nickname) {
    super(nickname, ErrorCode.NICKNAME_DUPLICATE);
  }
}
