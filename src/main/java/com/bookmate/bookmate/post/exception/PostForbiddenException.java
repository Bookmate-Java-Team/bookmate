package com.bookmate.bookmate.post.exception;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.common.error.exception.ForbiddenException;

public class PostForbiddenException extends ForbiddenException {

  public PostForbiddenException(final ErrorCode errorCode) {super(errorCode);}
}
