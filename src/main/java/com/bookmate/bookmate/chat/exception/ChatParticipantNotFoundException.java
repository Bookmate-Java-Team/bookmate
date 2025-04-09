package com.bookmate.bookmate.chat.exception;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.common.error.exception.NotFoundException;

public class ChatParticipantNotFoundException  extends NotFoundException {
  public ChatParticipantNotFoundException(final Long chatParticipantId) {
    super(chatParticipantId, ErrorCode.PARTICIPANT_NOT_FOUND);
  }
}
