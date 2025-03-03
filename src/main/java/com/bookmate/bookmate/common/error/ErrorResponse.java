package com.bookmate.bookmate.common.error;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

  private int status;
  private String code;
  private String message;
  private List<String> values = new ArrayList<>();

  private ErrorResponse(final int status, final ErrorCode code) {
    this.status = status;
    this.message = code.getMessage();
    this.code = code.getCode();
  }

  private ErrorResponse(final int status, final ErrorCode code, final String value) {
    this(status, code);
    this.values = List.of(value);
  }

  private ErrorResponse(final int status, final ErrorCode code, final List<String> values) {
    this(status, code);
    this.values = values;
  }

  public static ErrorResponse of(final int status, final ErrorCode code,
      final BindingResult bindingResult) {

    List<String> values = bindingResult.getFieldErrors().stream()
        .map(error -> {
          String field = error.getField(); // 필드명
          String defaultMessage = error.getDefaultMessage(); // @Size, @Email 등에서 정의한 메시지
          Object rejectedValue = error.getRejectedValue(); // 거절된 값

          if (rejectedValue == null) {
            return String.format("%s: %s", field, defaultMessage);
          } else {
            return String.format("%s: %s (입력값: %s)", field, defaultMessage, rejectedValue);
          }
        })
        .collect(Collectors.toList());
    return new ErrorResponse(status, code, values);
  }

  public static ErrorResponse of(final int status, final ErrorCode code) {
    return new ErrorResponse(status, code);
  }

  public static ErrorResponse of(final int status, final ErrorCode code,
      final String value) {
    return new ErrorResponse(status, code, value);
  }

  public static ErrorResponse of(int status, MethodArgumentTypeMismatchException e) {
    final String value = e.getValue() == null ? "" : e.getValue().toString();
    return new ErrorResponse(status, ErrorCode.INVALID_TYPE_VALUE, value);
  }
}
