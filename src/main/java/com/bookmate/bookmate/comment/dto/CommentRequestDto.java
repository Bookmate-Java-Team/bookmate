package com.bookmate.bookmate.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class  CommentRequestDto {

  private Long parentId;

  @NotBlank
  private String content;



}
