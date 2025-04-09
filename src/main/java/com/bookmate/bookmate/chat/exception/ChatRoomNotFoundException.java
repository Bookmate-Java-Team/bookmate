package com.bookmate.bookmate.chat.exception;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.common.error.exception.NotFoundException;

public class ChatRoomNotFoundException  extends NotFoundException {
  public ChatRoomNotFoundException(final Long chatRoomId) {
    super(chatRoomId, ErrorCode.CHAT_ROOM_NOT_FOUND);
  }
}
