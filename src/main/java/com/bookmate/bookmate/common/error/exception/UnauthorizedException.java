package com.bookmate.bookmate.common.error.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends BusinessException {

  private String value;

  public UnauthorizedException() {
    super("Unauthorized", ErrorCode.HANDLE_ACCESS_DENIED);
  }
}
