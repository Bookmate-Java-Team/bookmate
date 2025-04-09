package com.bookmate.bookmate.post.dto;

import com.bookmate.bookmate.post.entity.enums.PostCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequestDto {

  @NotBlank(message = "제목을 입력하세요.")
  @Schema(description = "제목", example = "책에 대한 나의 견해")
  private String title;

  @NotBlank(message = "내용을 입력하세요.")
  @Schema(description = "내용", example = "나의 생각은...")
  private String content;

  @NotNull(message = "카테고리를 입력하세요.")
  @Schema(description = "카테고리", allowableValues = {"FreeBookReview", "ReadingClubRecruitment", "FreeBoard"})
  private PostCategory category;


  @Schema(description = "모집 시작일 (ReadingClubRecruitment 카테고리일 때만 필요)", example = "2025-04-01", nullable = true)
  private LocalDate recruitStartDate;

  @Schema(description = "모집 종료일 (ReadingClubRecruitment 카테고리일 때만 필요)", example = "2025-04-10", nullable = true)
  private LocalDate recruitEndDate;

  @Schema(description = "모집 정원 (ReadingClubRecruitment 카테고리일 때만 필요)", example = "20", nullable = true)
  private Integer capacity;

  @Schema(description = "현재 참가자 수 (ReadingClubRecruitment 카테고리일 때만 필요)", example = "5", nullable = true)
  private Integer currentParticipants;

}
