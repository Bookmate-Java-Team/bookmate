package com.bookmate.bookmate.openAI.dto;



import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorrectionRequestDto {

  @NotBlank(message = "내용을 입력해주세요")
  private String content;

}
