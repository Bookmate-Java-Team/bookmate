package com.bookmate.bookmate.post.dto;

import com.bookmate.bookmate.post.entity.Post;
import com.bookmate.bookmate.post.entity.enums.PostCategory;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseDto {

  private String title;
  private String content;
  private String author;
  private PostCategory category;

  private LocalDate recruitStartDate;
  private LocalDate recruitEndDate;
  private Integer capacity;
  private Integer currentParticipants;

  public static PostResponseDto toDto(Post post) {
    if (post.getCategory() != PostCategory.ReadingClubRecruitment) {
      return PostResponseDto.builder().title(post.getTitle()).content(post.getContent())
          .author(post.getUser().getNickname()).category(post.getCategory()).build();
    } else {
      return PostResponseDto.builder().title(post.getTitle()).content(post.getContent())
          .author(post.getUser().getNickname()).category(post.getCategory())
          .recruitStartDate(post.getRecruitStartDate()).recruitEndDate(post.getRecruitEndDate())
          .capacity(post.getCapacity()).currentParticipants(post.getCurrentParticipants()).build();
    }
  }


}
