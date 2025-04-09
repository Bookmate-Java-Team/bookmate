package com.bookmate.bookmate.readingclub.exception;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.common.error.exception.ForbiddenException;

public class ReadingClubForbiddenException extends ForbiddenException {

  public ReadingClubForbiddenException(ErrorCode errorCode) {
    super(errorCode);
  }
}
