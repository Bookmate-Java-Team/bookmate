package com.bookmate.bookmate.comment.service;

import com.bookmate.bookmate.comment.dto.CommentRequestDto;
import com.bookmate.bookmate.comment.dto.CommentResponseDto;
import com.bookmate.bookmate.comment.entity.Comment;
import com.bookmate.bookmate.comment.exception.CommentDepthLimitExceededException;
import com.bookmate.bookmate.comment.exception.CommentForbiddenException;
import com.bookmate.bookmate.comment.exception.CommentNotFoundException;
import com.bookmate.bookmate.comment.repository.CommentRepository;
import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.post.entity.Post;
import com.bookmate.bookmate.post.exception.PostNotFoundException;
import com.bookmate.bookmate.post.repository.PostRepository;
import com.bookmate.bookmate.user.entity.User;
import com.bookmate.bookmate.user.exception.UserNotFoundException;
import com.bookmate.bookmate.user.repository.UserRepository;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private static final int MAX_DEPTH = 3;

  @Transactional
  public Comment addComment(Long userId, Long postId, CommentRequestDto commentRequestDto) {
    User user = findUserById(userId);
    Post post = findPostById(postId);

    Comment comment;
    if (commentRequestDto.getParentId() == null) {
      comment = commentRepository.save(
          Comment.builder().user(user).post(post).depth(1).content(commentRequestDto.getContent()).parent(null).build());
    } else {
      Comment parentComment = findCommentById(commentRequestDto.getParentId());
      int parentDepth = parentComment.getDepth();
      checkDepth(parentDepth);

      comment = commentRepository.save(
          Comment.builder().user(user).post(post).depth(parentDepth + 1).content(commentRequestDto.getContent()).parent(parentComment)
              .build());
    }

    return comment;
  }

  @Transactional
  public Comment updateComment(Long userId, Long commentId, @Valid CommentRequestDto commentRequestDto) {
    User user = findUserById(userId);
    Comment comment = findCommentById(commentId);

    if (comment.getUser() != user) {
      throw new CommentForbiddenException(ErrorCode.COMMENT_UPDATE_DENIED);
    }

    return comment.update(commentRequestDto);

  }

  @Transactional
  public void deleteComment(Long userId, Long commentId) {
    User user = findUserById(userId);
    Comment comment = findCommentById(commentId);

    if (comment.getUser() != user) {
      throw new CommentForbiddenException(ErrorCode.COMMENT_DELETE_DENIED);
    }

    comment.softDelete();

    deleteParentIfPossibleAndChildren(comment.getParent());
  }

  @Transactional(readOnly = true)
  public Page<CommentResponseDto> getComments(Long postId, Pageable pageable) {
    Post post = findPostById(postId);

    Page<Comment> parentComments = commentRepository.findAllByPostAndParentIsNullAndDeleteAtIsNull(post, pageable);

    return parentComments.map(this::convertToDtoWithChildren);
  }

  private CommentResponseDto convertToDtoWithChildren(Comment parentComment) {
    CommentResponseDto dto = CommentResponseDto.toDto(parentComment);

    List<Comment> children = commentRepository.findAllByParent(parentComment);

    List<CommentResponseDto> childrenDto = children.stream()
        .map(this::convertToDtoWithChildren)
        .toList();
    dto.setChildren(childrenDto);
    return dto;
  }

  private User findUserById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  private Post findPostById(Long id) {
    return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
  }

  private Comment findCommentById(Long id) {
    return commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(id));
  }

  private void checkDepth(int depth) {
    if (depth >= MAX_DEPTH) {
      throw new CommentDepthLimitExceededException();
    }
  }

  private void deleteParentIfPossibleAndChildren(Comment parentComment) {
    if (parentComment == null) {
      return;
    }

    if (parentComment.getDeleteAt() == null) {
      return;
    }

    boolean allChildrenDeleted = parentComment.getChildren().stream()
        .allMatch(child -> child.getDeleteAt() != null);

    if (allChildrenDeleted) {

      // 참조 무결성 제약 해결
      // 자식 댓글 삭제
      commentRepository.deleteChildren(parentComment.getId());

      // 부모 댓글 삭제
      commentRepository.deleteParent(parentComment.getId());
    }
  }

}
