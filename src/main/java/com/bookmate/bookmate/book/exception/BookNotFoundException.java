package com.bookmate.bookmate.book.exception;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.common.error.exception.NotFoundException;

public class BookNotFoundException extends NotFoundException {

  public BookNotFoundException(Long value) {
    super(value, ErrorCode.BOOK_NOT_FOUND);
  }

  public BookNotFoundException(String value) {
    super(value, ErrorCode.BOOK_NOT_FOUND);
  }
}
