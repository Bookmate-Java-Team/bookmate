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
  REFRESH_TOKEN_MISMATCH("U-009", "Refresh Token mismatch. Re-login required");

  private final String code;
  private final String message;

  ErrorCode(final String code, final String message) {
    this.code = code;
    this.message = message;
  }
}
