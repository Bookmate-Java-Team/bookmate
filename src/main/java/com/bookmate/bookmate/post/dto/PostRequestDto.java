package com.bookmate.bookmate.post.dto;

import com.bookmate.bookmate.post.entity.Post;
import com.bookmate.bookmate.post.entity.enums.PostCategory;
import jakarta.validation.constraints.AssertTrue;
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
  private String title;

  @NotBlank(message = "내용을 입력하세요.")
  private String content;

  @NotNull(message = "카테고리를 입력하세요.")
  private PostCategory category;

  private LocalDate recruitStartDate;
  private LocalDate recruitEndDate;
  private Integer capacity;
  private Integer currentParticipants;

}
