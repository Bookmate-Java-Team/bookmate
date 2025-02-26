package com.bookmate.bookmate.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TestExceptionController.class)
public class GlobalExceptionHandlerTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("409 CONFLICT - DuplicateException 핸들링 테스트")
  @WithMockUser
  void handleDuplicateException() throws Exception {
    mockMvc.perform(get("/test-exception/duplicate"))
        .andExpect(status().isConflict()) // 409 상태 코드 확인
        .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATE.getCode()))
        .andExpect(jsonPath("$.message").value(ErrorCode.DUPLICATE.getMessage()));
  }

  @Test
  @DisplayName("404 NOT FOUND - NotFoundException 핸들링 테스트")
  @WithMockUser
  void handleNotFoundException() throws Exception {
    mockMvc.perform(get("/test-exception/not-found"))
        .andExpect(status().isNotFound()) // 404 상태 코드 확인
        .andExpect(jsonPath("$.code").value(ErrorCode.NOT_FOUND.getCode()))
        .andExpect(jsonPath("$.message").value(ErrorCode.NOT_FOUND.getMessage()));
  }

  @Test
  @DisplayName("401 UNAUTHORIZED - UnauthorizedException 핸들링 테스트")
  @WithMockUser
  void handleUnauthorizedException() throws Exception {
    mockMvc.perform(get("/test-exception/unauthorized"))
        .andExpect(status().isUnauthorized()) // 401 상태 코드 확인
        .andExpect(jsonPath("$.code").value(ErrorCode.HANDLE_ACCESS_DENIED.getCode()))
        .andExpect(jsonPath("$.message").value(ErrorCode.HANDLE_ACCESS_DENIED.getMessage()));
  }

  @Test
  @DisplayName("403 FORBIDDEN - ForbiddenException 핸들링 테스트")
  @WithMockUser
  void handleForbiddenException() throws Exception {
    mockMvc.perform(get("/test-exception/forbidden"))
        .andExpect(status().isForbidden()) // 403 상태 코드 확인
        .andExpect(jsonPath("$.code").value(ErrorCode.HANDLE_ACCESS_DENIED.getCode()))
        .andExpect(jsonPath("$.message").value(ErrorCode.HANDLE_ACCESS_DENIED.getMessage()));
  }

  @Test
  @DisplayName("500 INTERNAL SERVER ERROR - 서버 오류 핸들링 테스트")
  @WithMockUser
  void handleInternalServerError() throws Exception {
    mockMvc.perform(get("/test-exception/server-error"))
        .andExpect(status().isInternalServerError()) // 500 상태 코드 확인
        .andExpect(jsonPath("$.code").value(ErrorCode.INTERNAL_SERVER_ERROR.getCode()))
        .andExpect(jsonPath("$.message").value(ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
  }

}
