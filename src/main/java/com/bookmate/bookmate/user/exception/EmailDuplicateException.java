package com.bookmate.bookmate.user.exception;

import com.bookmate.bookmate.common.error.exception.DuplicateException;
import com.bookmate.bookmate.common.error.exception.ErrorCode;

public class EmailDuplicateException extends DuplicateException {

  public EmailDuplicateException(final String email) {
    super(email, ErrorCode.EMAIL_DUPLICATE);
  }
}
