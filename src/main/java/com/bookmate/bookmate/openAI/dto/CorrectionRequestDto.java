package com.bookmate.bookmate.openAI.dto;



import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorrectionRequestDto {

  @NotBlank(message = "내용을 입력해주세요")
  @Schema(description = "교정할 문장", example = "I has a apple.")
  private String content;

}
