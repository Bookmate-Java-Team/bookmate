package com.bookmate.bookmate.comment.exception;

import com.bookmate.bookmate.common.error.exception.BusinessException;
import com.bookmate.bookmate.common.error.exception.ErrorCode;

public class CommentDepthLimitExceededException extends BusinessException {


  public CommentDepthLimitExceededException() {
    super(ErrorCode.COMMENT_DEPTH_LIMIT_EXCEEDED);
  }
}
