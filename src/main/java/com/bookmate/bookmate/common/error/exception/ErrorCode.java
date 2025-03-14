package com.bookmate.bookmate.common.error.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
  // Common
  INVALID_INPUT_VALUE("C-001", "Invalid Input Value"),
  METHOD_NOT_ALLOWED("C-002", "Method Not Allowed"),
  INTERNAL_SERVER_ERROR("C-003", "Server Error"),
  INVALID_TYPE_VALUE("C-004", "Invalid Type Value"),
  HANDLE_ACCESS_DENIED("C-005", "Access is Denied"),

  // Business
  DUPLICATE("B-001", "Duplicate Value"),
  NOT_FOUND("B-002", "Entity Not Found"),

  // User
  EMAIL_DUPLICATE("U-001", "Duplicate Email Address"),
  NICKNAME_DUPLICATE("U-002", "Duplicate Nickname"),
  WRONG_EMAIL("U-003", "Wrong Email"),
  PASSWORD_MISMATCH("U-004", "Password mismatch"),
  USER_NOT_FOUND("U-005", "User Not Found"),
  USER_DEACTIVATED("U-006", "User has been deactivated"),
  ACCESS_TOKEN_NOT_EXPIRED_YET("U-007", "AccessToken is not expired yet"),
  INVALID_REFRESH_TOKEN("U-008", "Invalid Refresh Token"),
  REFRESH_TOKEN_MISMATCH("U-009", "Refresh Token mismatch. Re-login required"),

  // Review
  REVIEW_NOT_FOUND("R-001", "Review Not Found"),
  REVIEW_PERMISSION_DENIED("R-002", "You do not have permission to write a review"),
  DUPLICATE_REVIEW("R-003", "You have already written a review for this book"),
  REVIEW_UPDATE_DENIED("R-004", "You do not have permission to update this review"),
  REVIEW_DELETE_DENIED("R-005", "You do not have permission to delete this review"),

  // Book
  BOOK_NOT_FOUND("BOOK-001", "Book Not Found"),

  // Post
  POST_NOT_FOUND("P-001", "Post Not Found"),
  POST_UPDATE_DENIED("P-002", "You do not have permission to update this post"),
  POST_DELETE_DENIED("P-003", "You do not have permission to delete this post"),

  // Like
  LIKE_NOT_FOUND("L-001", "Like Not Found"),
  DUPLICATE_LIKE("L-002", "Duplicate Like"),

  // Comment
  COMMENT_NOT_FOUND("COMMENT-001", "Comment Not Found"),
  COMMENT_DEPTH_LIMIT_EXCEEDED("COMMENT-002", "Comment Depth Limit Exceeded"),
  COMMENT_UPDATE_DENIED("COMMENT-003", "You do not have permission to update this comment"),
  COMMENT_DELETE_DENIED("COMMENT-004", "You do not have permission to delete this comment"),

  // ReadingClub
  READING_CLUB_NOT_FOUND("RC-001", "Reading Club Not Found"),
  READING_CLUB_UPDATE_DENIED("RC-002", "You do not have permission to update this post"),
  READING_CLUB_DELETE_DENIED("RC-003", "You do not have permission to delete this post"),
  READING_CLUB_DUPLICATE_JOIN("RC-004", "Duplicate join"),
  READING_CLUB_NOT_JOINED("RC-005", "ReadingClub not joined"),
  READING_CLUB_FULL("RC-006", "ReadingClub full"),
  READING_CLUB_INVALID_STATUS("RC-007", "ReadingClub invalid status"),

  // Chat
  CHAT_ROOM_NOT_FOUND("CH-001", "채팅방을 찾을 수 없습니다."),
  MESSAGE_NOT_FOUND("CH-002", "메시지를 찾을 수 없습니다."),
  PARTICIPANT_NOT_FOUND("CH-003", "채팅 참여자를 찾을 수 없습니다."),
  ;

  private final String code;
  private final String message;

  ErrorCode(final String code, final String message) {
    this.code = code;
    this.message = message;
  }
}
