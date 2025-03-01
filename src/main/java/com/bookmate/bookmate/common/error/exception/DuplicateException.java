package com.bookmate.bookmate.common.error.exception;

import lombok.Getter;

@Getter
public class DuplicateException extends BusinessException {

  private final String value;

  public DuplicateException(final String value) {
    this(value, ErrorCode.DUPLICATE);
  }

  public DuplicateException(final String value, final ErrorCode errorCode) {
    super(value, errorCode);
    this.value = value;
  }
}
