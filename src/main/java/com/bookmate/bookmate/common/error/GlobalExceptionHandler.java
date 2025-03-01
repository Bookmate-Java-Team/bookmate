package com.bookmate.bookmate.common.error;

import com.bookmate.bookmate.common.error.exception.BusinessException;
import com.bookmate.bookmate.common.error.exception.DuplicateException;
import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.common.error.exception.ForbiddenException;
import com.bookmate.bookmate.common.error.exception.NotFoundException;
import com.bookmate.bookmate.common.error.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Validated 시 바인딩 에러가 존재할 때 발생
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      final MethodArgumentNotValidException e) {
    log.info("Handle MethodArgumentNotValidException", e);

    final int status = HttpStatus.BAD_REQUEST.value();
    final ErrorResponse response = ErrorResponse.of(status, ErrorCode.INVALID_INPUT_VALUE,
        e.getBindingResult());

    return new ResponseEntity<>(response, HttpStatus.valueOf(status));
  }

  /**
   * RequestParam 타입이 맞지 않을 때 발생
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
      final MethodArgumentTypeMismatchException e) {
    log.info("Handle MethodArgumentTypeMismatchException", e);

    final int status = HttpStatus.BAD_REQUEST.value();
    final ErrorResponse response = ErrorResponse.of(status, e);

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * 지원하지 않는 HTTP 메서드로 요청 시
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
      final HttpRequestMethodNotSupportedException e) {
    log.info("Handle HttpRequestMethodNotSupportedException", e);

    final int status = HttpStatus.METHOD_NOT_ALLOWED.value();
    final ErrorResponse response = ErrorResponse.of(status, ErrorCode.METHOD_NOT_ALLOWED);

    return new ResponseEntity<>(response, HttpStatus.valueOf(status));
  }

  /**
   * JSON Request Body 에서 type mismatch 가 발생할 때
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
      final HttpMessageNotReadableException e) {
    log.info("Handle HttpMessageNotReadableException", e);

    final int status = HttpStatus.BAD_REQUEST.value();
    final ErrorResponse response = ErrorResponse.of(status, ErrorCode.INVALID_TYPE_VALUE);

    return new ResponseEntity<>(response, HttpStatus.valueOf(status));
  }

  /**
   * 서버에서 발생하는 모든 예상치 못한 예외 처리
   */
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleException(final Exception e) {
    log.error("Handle Exception", e);

    final int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
    final ErrorResponse response = ErrorResponse.of(status, ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());

    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * 비즈니스 로직에서 발생하는 예외 처리
   */
  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
    log.info("Handle BusinessException", e);

    final ErrorCode errorCode = e.getErrorCode();
    final int status = HttpStatus.BAD_GATEWAY.value();
    final ErrorResponse response = ErrorResponse.of(status, errorCode);

    return new ResponseEntity<>(response, HttpStatus.valueOf(status));
  }

  /**
   * 중복 예외 (409 CONFLICT)
   */
  @ExceptionHandler(DuplicateException.class)
  protected ResponseEntity<ErrorResponse> handleDuplicationException(final DuplicateException e) {
    log.info("Handle DuplicationException", e);

    final ErrorCode errorCode = e.getErrorCode();
    final String value = e.getValue();
    final int status = HttpStatus.CONFLICT.value();
    final ErrorResponse response = (value != null)
        ? ErrorResponse.of(status, errorCode, value)
        : ErrorResponse.of(status, errorCode);

    return new ResponseEntity<>(response, HttpStatus.valueOf(status));
  }

  /**
   * 리소스를 찾을 수 없을 때 (404 NOT FOUND)
   */
  @ExceptionHandler(NotFoundException.class)
  protected ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundException e) {
    log.info("Handle NotFoundException", e);

    final int status = HttpStatus.NOT_FOUND.value();
    final ErrorResponse response = ErrorResponse.of(status, e.getErrorCode());

    return new ResponseEntity<>(response, HttpStatus.valueOf(status));
  }

  /**
   * 인증 오류 (401 UNAUTHORIZED)
   */
  @ExceptionHandler(UnauthorizedException.class)
  protected ResponseEntity<ErrorResponse> handleUnauthorizedException(final UnauthorizedException e) {
    log.info("Handle UnauthorizedException", e);

    final int status = HttpStatus.UNAUTHORIZED.value();
    final ErrorResponse response = ErrorResponse.of(status, e.getErrorCode());

    return new ResponseEntity<>(response, HttpStatus.valueOf(status));
  }

  /**
   * 권한이 없는 사용자가 접근하려고 할 때 (403 FORBIDDEN)
   */
  @ExceptionHandler(ForbiddenException.class)
  protected ResponseEntity<ErrorResponse> handleForbiddenException(final ForbiddenException e) {
    log.info("Handle ForbiddenException", e);

    final int status = HttpStatus.FORBIDDEN.value();
    final ErrorResponse response = ErrorResponse.of(status, e.getErrorCode());

    return new ResponseEntity<>(response, HttpStatus.valueOf(status));
  }
}
