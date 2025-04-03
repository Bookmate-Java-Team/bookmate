package com.bookmate.bookmate.openAI.controller;

import com.bookmate.bookmate.openAI.dto.CorrectionRequestDto;
import com.bookmate.bookmate.openAI.service.OpenAIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/openai")
@RequiredArgsConstructor
@Tag(name = "OpenAI API", description = "OpenAI를 활용한 문장 교정 API")
public class OpenAIController {

  private final OpenAIService openAIService;

  @GetMapping("/correction")
  @Operation(summary = "문장 교정", description = "입력된 문장을 OpenAI를 활용하여 문법적으로 올바르게 수정합니다.")
  @ApiResponse(responseCode = "200", description = "교정된 문장 반환 성공")
  public ResponseEntity<String> correctionContent(@Valid CorrectionRequestDto correctionRequestDto) {
    return ResponseEntity.ok(openAIService.correctionContent(correctionRequestDto));
  }

}
