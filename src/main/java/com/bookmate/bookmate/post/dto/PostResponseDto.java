package com.bookmate.bookmate.post.dto;

import com.bookmate.bookmate.post.entity.Post;
import com.bookmate.bookmate.post.entity.enums.PostCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseDto {


  @Schema(description = "제목", example = "책에 대한 나의 견해")
  private String title;

  @Schema(description = "내용", example = "나의 생각은...")
  private String content;

  @Schema(description = "작성자", example = "홍길동")
  private String author;

  @Schema(description = "좋아요 수", example = "15")
  private Integer likes;

  @Schema(description = "카테고리", allowableValues = {"FreeBookReview", "ReadingClubRecruitment", "FreeBoard"})
  private PostCategory category;

  @Schema(description = "작성일", example = "2025-03-29T14:37:04.192Z")
  private LocalDateTime createdAt;

  @Schema(description = "모집 시작일 (ReadingClubRecruitment 카테고리일 때만 표시)",
      example = "2025-04-01", nullable = true)
  private LocalDate recruitStartDate;

  @Schema(description = "모집 종료일 (ReadingClubRecruitment 카테고리일 때만 표시)",
      example = "2025-04-10", nullable = true)
  private LocalDate recruitEndDate;

  @Schema(description = "모집 정원 (ReadingClubRecruitment 카테고리일 때만 표시)",
      example = "20", nullable = true)
  private Integer capacity;

  @Schema(description = "현재 참가자 수 (ReadingClubRecruitment 카테고리일 때만 표시)",
      example = "5", nullable = true)
  private Integer currentParticipants;

  public PostResponseDto(List<PostResponseDto> content, int pageNumber, int pageSize, long totalElements, int totalPages, boolean first, boolean last) {
  }

  public static PostResponseDto toDto(Post post) {
    if (post.getCategory() != PostCategory.ReadingClubRecruitment) {
      return PostResponseDto.builder().title(post.getTitle()).content(post.getContent())
          .author(post.getUser().getNickname()).category(post.getCategory()).likes(post.getTotalLikes()).createdAt(post.getCreatedAt()).build();
    } else {
      return PostResponseDto.builder().title(post.getTitle()).content(post.getContent())
          .author(post.getUser().getNickname()).category(post.getCategory())
          .recruitStartDate(post.getRecruitStartDate()).recruitEndDate(post.getRecruitEndDate())
          .capacity(post.getCapacity()).currentParticipants(post.getCurrentParticipants()).likes(post.getTotalLikes()).createdAt(post.getCreatedAt()).build();
    }
  }


}
