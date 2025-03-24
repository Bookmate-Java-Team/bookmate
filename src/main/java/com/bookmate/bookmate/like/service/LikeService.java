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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

  private final LikeRepository likeRepository;
  private final UserRepository userRepository;
  private final RedisLikeService redisLikeService;
  public static final String LIKE_ADDED = "좋아요 추가됨: userId=%d, targetId=%d, targetType=%s";
  public static final String LIKE_DELETED = "좋아요 취소됨: userId=%d, targetId=%d, targetType=%s";

  @Transactional(readOnly = true)
  public int getLikeCount(Long targetId, TargetType targetType) {
    return likeRepository.countLikeByTargetIdAndTargetType(targetId, targetType);
  }

  private User findUserById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  @Transactional
  public String toggleLike(Long userId, @Valid LikeRequestDto likeRequestDto) {
    User user = findUserById(userId);
    TargetType targetType = likeRequestDto.getTargetType();
    Long targetId = likeRequestDto.getTargetId();

    boolean hasLiked = redisLikeService.hasLiked(userId, targetType, targetId);

    // redis에 좋아요 정보가 없으면 db에서 확인
    if (!hasLiked) {
      hasLiked = likeRepository.existsByUserAndTargetTypeAndTargetId(user, targetType, targetId);
    }

    // db에 좋아요 정보가 없으면
    if (!hasLiked) {
      redisLikeService.addLike(userId, targetType, targetId);
      return String.format(LIKE_ADDED, userId, targetId, targetType);
    } else {
      redisLikeService.deleteLike(userId, targetType, targetId);
      return String.format(LIKE_DELETED, userId, targetId, targetType);
    }
  }
}
