package com.bookmate.bookmate.comment.dto;

import com.bookmate.bookmate.comment.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
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

  @Schema(description = "댓글 작성자", example = "user123")
  private String author;

  @Schema(description = "댓글 내용", example = "이 책 정말 재미있네요!")
  private String content;

  @Schema(description = "댓글 작성 시간", example = "2025-03-29T14:37:04.192")
  private LocalDateTime createdAt;

  @Schema(description = "대댓글 리스트", example = "[]")
  private List<CommentResponseDto> children = new ArrayList<>();

  public static CommentResponseDto toDto(Comment comment) {
    return CommentResponseDto.builder().author(comment.getUser().getNickname())
        .content(comment.getContent()).createdAt(comment.getCreatedAt()).build();
  }
}
