package com.bookmate.bookmate.exception;

import com.bookmate.bookmate.common.error.exception.DuplicateException;
import com.bookmate.bookmate.common.error.exception.ForbiddenException;
import com.bookmate.bookmate.common.error.exception.NotFoundException;
import com.bookmate.bookmate.common.error.exception.UnauthorizedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-exception")
public class TestExceptionController {

  @GetMapping("/duplicate")
  public void throwDuplicateException() {
    throw new DuplicateException("Duplicate test value");
  }

  @GetMapping("/not-found")
  public void throwNotFoundException() {
    throw new NotFoundException("Entity not found");
  }

  @GetMapping("/unauthorized")
  public void throwUnauthorizedException() {
    throw new UnauthorizedException();
  }

  @GetMapping("/forbidden")
  public void throwForbiddenException() {
    throw new ForbiddenException();
  }

  @GetMapping("/server-error")
  public void throwServerErrorException() {
    throw new RuntimeException("Unexpected server error");
  }
}
