package com.bookmate.bookmate.common.error.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends BusinessException {

  private String value;

  public NotFoundException(String value) {
    this(value, ErrorCode.NOT_FOUND);
    this.value = value;
  }

  public NotFoundException(String value, ErrorCode errorCode) {
    super(value, errorCode);
    this.value = value;
  }
}
