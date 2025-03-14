package com.bookmate.bookmate.comment.exception;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.common.error.exception.NotFoundException;

public class CommentNotFoundException extends NotFoundException {


  public CommentNotFoundException(Long value) {
    super(value, ErrorCode.COMMENT_NOT_FOUND);
  }
}
