package com.bookmate.bookmate.openAI.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAIRequest {
  private String model;
  private List<Message> messages;

  public OpenAIRequest(String model, String content) {
    this.model = model;
    this.messages = new ArrayList<>();
    this.messages.add(new Message("user", content));
  }

  public void setSystemPromptConfig(String prompt) {
    this.messages.add(new Message("system", prompt));
  }

}
