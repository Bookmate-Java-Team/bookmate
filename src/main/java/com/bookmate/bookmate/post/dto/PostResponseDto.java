package com.bookmate.bookmate.post.dto;

import com.bookmate.bookmate.post.entity.Post;
import com.bookmate.bookmate.post.entity.enums.PostCategory;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
  private Integer likes;
  private PostCategory category;
  private LocalDateTime createdAt;

  private LocalDate recruitStartDate;
  private LocalDate recruitEndDate;
  private Integer capacity;
  private Integer currentParticipants;

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
