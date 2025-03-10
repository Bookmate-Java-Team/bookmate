package com.bookmate.bookmate.post.service;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.post.dto.PostRequestDto;
import com.bookmate.bookmate.post.dto.PostUpdateRequestDto;
import com.bookmate.bookmate.post.entity.Post;
import com.bookmate.bookmate.post.entity.enums.PostCategory;
import com.bookmate.bookmate.post.exception.PostForbiddenException;
import com.bookmate.bookmate.post.exception.PostNotFoundException;
import com.bookmate.bookmate.post.repository.PostRepository;
import com.bookmate.bookmate.user.entity.User;
import com.bookmate.bookmate.user.exception.UserNotFoundException;
import com.bookmate.bookmate.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;

  @Transactional
  public Post createPost(Long userId, @Valid PostRequestDto postRequestDto) {
    User user = findUserById(userId);
    Post post;
    if (isRecruitmentValid(postRequestDto)) {
      post = postRepository.save(
          Post.builder().title(postRequestDto.getTitle()).content(postRequestDto.getContent())
              .category(postRequestDto.getCategory()).user(user).build());
    } else {
      post = postRepository.save(
          Post.builder().title(postRequestDto.getTitle()).content(postRequestDto.getContent())
              .category(postRequestDto.getCategory()).user(user)
              .recruitStartDate(postRequestDto.getRecruitStartDate()).build());
    }

    return post;
  }

  @Transactional
  public Post updatePost(Long userId, Long postId,
      @Valid PostUpdateRequestDto postUpdateRequestDto) {
    User user = findUserById(userId);
    Post post = findPostById(postId);

    if (post.getUser() != user) {
      throw new PostForbiddenException(ErrorCode.POST_UPDATE_DENIED);
    }

    return post.updatePost(postUpdateRequestDto);
  }

  @Transactional
  public void deletePost(Long userId, Long postId) {
    User user = findUserById(userId);
    Post post = findPostById(postId);

    if (post.getUser() != user) {
      throw new PostForbiddenException(ErrorCode.POST_DELETE_DENIED);
    }

    postRepository.delete(post);
  }

  @Transactional(readOnly = true)
  public Post getPost(Long postId) {
    return findPostById(postId);
  }

  @Transactional(readOnly = true)
  public Page<Post> getAllPosts(Pageable pageable) {
    return postRepository.findAll(pageable);
  }


  /**
   * 모집 관련 필드 유효성 검증
   * - category가 "ReadingClubRecruitment"일 때만 모집 관련 필드가 필수
   * - 그 외 카테고리에서는 NULL이어야 함.
   */
  private boolean isRecruitmentValid(PostRequestDto postRequestDto) {
    if (postRequestDto.getCategory() == PostCategory.ReadingClubRecruitment) {
      return postRequestDto.getRecruitStartDate() != null
          && postRequestDto.getRecruitEndDate() != null && postRequestDto.getCapacity() != null
          && postRequestDto.getCurrentParticipants() != null;
    }
    return false;
  }

  private User findUserById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  private Post findPostById(Long id) {
    return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
  }

}
