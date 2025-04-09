package com.bookmate.bookmate.readingclub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 독서모임 생성/수정 요청 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadingClubRequestDto {


  @Schema(description = "독서모임 제목", example = "철학 읽는 밤")
  @NotBlank(message = "독서모임 제목을 입력하세요.")
  @Size(min = 2, max = 50, message = "독서모임 제목은 2~50자 이내여야 합니다.")
  private String title;

  @Schema(description = "독서모임 설명", example = "함께 철학 책을 읽고 토론하는 모임입니다.")
  @NotBlank(message = "독서모임 설명을 입력하세요.")
  @Size(min = 10, max = 500, message = "독서모임 설명은 10~500자 이내여야 합니다.")
  private String description;

  @Schema(description = "최대 참가 인원", example = "10")
  @Min(value = 1, message = "최소 참가 인원은 1명 이상이어야 합니다.")
  @Max(value = 30, message = "최대 참가 인원은 30명 이하이어야 합니다.")
  private int maxParticipants;
}
