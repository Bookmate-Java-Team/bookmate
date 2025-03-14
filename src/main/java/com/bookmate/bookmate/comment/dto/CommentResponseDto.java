package com.bookmate.bookmate.comment.dto;

import com.bookmate.bookmate.comment.entity.Comment;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {

  private String author;

  private String content;

  private LocalDateTime createdAt;

  private List<CommentResponseDto> children = new ArrayList<>();

  public static CommentResponseDto toDto(Comment comment) {
    return CommentResponseDto.builder().author(comment.getUser().getNickname())
        .content(comment.getContent()).createdAt(comment.getCreatedAt()).build();
  }
}
