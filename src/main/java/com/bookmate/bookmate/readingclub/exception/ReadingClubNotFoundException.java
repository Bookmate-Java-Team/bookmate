package com.bookmate.bookmate.readingclub.exception;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.common.error.exception.NotFoundException;

public class ReadingClubNotFoundException extends NotFoundException {

  public ReadingClubNotFoundException(final Long id) {
    super(id, ErrorCode.READING_CLUB_NOT_FOUND);
  }
}
