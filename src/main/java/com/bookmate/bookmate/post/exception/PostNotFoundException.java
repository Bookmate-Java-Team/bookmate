package com.bookmate.bookmate.post.exception;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.common.error.exception.NotFoundException;

public class PostNotFoundException extends NotFoundException {


  public PostNotFoundException(Long value) {
    super(value, ErrorCode.POST_NOT_FOUND);
  }
}
