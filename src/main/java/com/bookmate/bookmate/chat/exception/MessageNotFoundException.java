package com.bookmate.bookmate.chat.exception;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.common.error.exception.NotFoundException;

public class MessageNotFoundException  extends NotFoundException {
  public MessageNotFoundException(final Long messageId) {
    super(messageId, ErrorCode.MESSAGE_NOT_FOUND);
  }
}
