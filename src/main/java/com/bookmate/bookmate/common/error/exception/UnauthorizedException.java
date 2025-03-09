package com.bookmate.bookmate.common.error.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends BusinessException {

  public UnauthorizedException() {
    super(ErrorCode.HANDLE_ACCESS_DENIED);
  }

  public UnauthorizedException(final ErrorCode errorCode) {
    super(errorCode);
  }
}
