package com.bookmate.bookmate.common.error.exception;

import lombok.Getter;

@Getter
public class ForbiddenException extends BusinessException {

  public ForbiddenException() {
    super("Forbidden", ErrorCode.HANDLE_ACCESS_DENIED);
  }
}
