package com.bookmate.bookmate.openAI.controller;

import com.bookmate.bookmate.openAI.dto.CorrectionRequestDto;
import com.bookmate.bookmate.openAI.dto.OpenAIRequest;
import com.bookmate.bookmate.openAI.dto.OpenAIResponse;
import com.bookmate.bookmate.openAI.service.OpenAIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/openai")
@RequiredArgsConstructor
public class OpenAIController {

  private final OpenAIService openAIService;

  @GetMapping("/correction")
  public ResponseEntity<String> correctionContent(@Valid CorrectionRequestDto correctionRequestDto) {
    return ResponseEntity.ok(openAIService.correctionContent(correctionRequestDto));
  }

}
