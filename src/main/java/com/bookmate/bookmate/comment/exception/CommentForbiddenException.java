package com.bookmate.bookmate.comment.exception;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.common.error.exception.ForbiddenException;

public class CommentForbiddenException extends ForbiddenException {

  public CommentForbiddenException(ErrorCode errorCode) {
    super(errorCode);
  }
}
