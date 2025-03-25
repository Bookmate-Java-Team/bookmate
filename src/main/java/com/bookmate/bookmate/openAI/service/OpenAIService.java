package com.bookmate.bookmate.openAI.service;

import com.bookmate.bookmate.openAI.dto.CorrectionRequestDto;
import com.bookmate.bookmate.openAI.dto.OpenAIRequest;
import com.bookmate.bookmate.openAI.dto.OpenAIResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OpenAIService {

  @Value("${openai.model}")
  private String model;

  @Value("${openai.api.url}")
  private String apiURL;

  private final RestTemplate restTemplate;

  public String correctionContent(CorrectionRequestDto correctionRequestDto) {
    OpenAIRequest request = new OpenAIRequest(model, correctionRequestDto.getContent());

    String correctionPrompt = "당신은 사용자가 작성한 책 리뷰 또는 게시판 글을 개선하는 역할을 합니다.  \n"
        + "사용자의 입력을 받아 **맞춤법 검사, 가독성 개선, 감정적 표현 완화**를 수행한 후, 수정된 텍스트를 반환합니다.  \n"
        + "수정된 결과는 `[[[ 수정된 내용 ]]]` 형식으로 감싸서 출력합니다.  \n"
        + "원본 내용은 반환하지 않으며, 내용의 의미를 변경하지 않도록 주의합니다.  ";

    request.setSystemPromptConfig(correctionPrompt);

    OpenAIResponse response = restTemplate.postForObject(apiURL, request, OpenAIResponse.class);

    return extractProcessedText(response.getChoices().get(0).getMessage().getContent());

  }

  private String extractProcessedText(String input) {
    Pattern pattern = Pattern.compile("\\[\\[\\[(.*?)]]]", Pattern.DOTALL);
    Matcher matcher = pattern.matcher(input);
    return matcher.find() ? matcher.group(1).trim() : "수정된 내용을 찾을 수 없습니다.";
  }
}
