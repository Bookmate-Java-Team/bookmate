package com.bookmate.bookmate.readingclub.exception;

import com.bookmate.bookmate.common.error.exception.BusinessException;
import com.bookmate.bookmate.common.error.exception.ErrorCode;

public class ReadingClubFullException extends BusinessException {

  public ReadingClubFullException() {
    super(ErrorCode.READING_CLUB_FULL);
  }
}
