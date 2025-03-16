package com.bookmate.bookmate.like.service;

import com.bookmate.bookmate.like.dto.LikeRequestDto;
import com.bookmate.bookmate.like.entity.enums.TargetType;
import com.bookmate.bookmate.like.exception.LikeDuplicateException;
import com.bookmate.bookmate.like.exception.LikeNotFoundException;
import com.bookmate.bookmate.like.repository.LikeRepository;
import com.bookmate.bookmate.post.repository.PostRepository;
import com.bookmate.bookmate.user.entity.User;
import com.bookmate.bookmate.user.exception.UserNotFoundException;
import com.bookmate.bookmate.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

  private final LikeRepository likeRepository;
  private final UserRepository userRepository;
  private final RedisLikeService redisLikeService;

  public String addLike(Long userId, LikeRequestDto likeRequestDto) {
    User user = findUserById(userId);
    TargetType targetType = likeRequestDto.getTargetType();
    Long targetId = likeRequestDto.getTargetId();

    if (redisLikeService.hasLiked(userId, targetType, targetId)) {
      throw new LikeDuplicateException("userId: " + userId);
    }

    redisLikeService.addLike(userId, targetType, targetId);

    return "userId: " + userId + ", targetId: " + targetId + ", type: " + targetType;
  }


  @Transactional
  public void deleteLike(Long userId, @Valid LikeRequestDto likeRequestDto) {
    User user = findUserById(userId);
    TargetType targetType = likeRequestDto.getTargetType();
    Long targetId = likeRequestDto.getTargetId();

    if (redisLikeService.hasLiked(userId, targetType, targetId)) {
      redisLikeService.deleteLike(userId, targetType, targetId);
    } else {
      throw new LikeNotFoundException("userId: " + userId);
    }

  }

  @Transactional(readOnly = true)
  public int getLikeCount(Long targetId, TargetType targetType) {
    return likeRepository.countLikeByTargetIdAndTargetType(targetId, targetType);
  }

  private User findUserById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

}
