package com.bookmate.bookmate.common.error.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

  private final ErrorCode errorCode;

  public BusinessException(final ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  @Override
  public synchronized Throwable fillInStackTrace() {
    // 성능 최적화를 위해 스택 트레이스를 채우지 않음
    return this;
  }
}
