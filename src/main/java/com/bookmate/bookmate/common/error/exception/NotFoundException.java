package com.bookmate.bookmate.common.error.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends BusinessException {

  private final String value;

  public NotFoundException(final String value) {
    this(value, ErrorCode.NOT_FOUND);
  }

  public NotFoundException(final String value, final ErrorCode errorCode) {
    super(errorCode);
    this.value = value;
  }

  public NotFoundException(final Long value) {
    this(value, ErrorCode.NOT_FOUND);
  }

  public NotFoundException(final Long value, final ErrorCode errorCode) {
    super(errorCode);
    this.value = value.toString();
  }
}
