package com.bookmate.bookmate.like.service;

import com.bookmate.bookmate.like.dto.LikeRequestDto;
import com.bookmate.bookmate.like.entity.Like;
import com.bookmate.bookmate.like.entity.enums.TargetType;
import com.bookmate.bookmate.like.exception.LikeDuplicateException;
import com.bookmate.bookmate.like.exception.LikeNotFoundException;
import com.bookmate.bookmate.like.repository.LikeRepository;
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

  @Transactional
  public Like addLike(Long userId, @Valid LikeRequestDto likeRequestDto) {
    User user = findUserById(userId);

    if (likeRepository.existsByUserAndTargetIdAndTargetType(user,
        likeRequestDto.getTargetId(), likeRequestDto.getTargetType())) {
      throw new LikeDuplicateException("userId: " + userId);
    }

    return likeRepository.save(Like.builder().user(user).targetId(likeRequestDto.getTargetId())
        .targetType(likeRequestDto.getTargetType()).build());
  }


  @Transactional
  public void deleteLike(Long userId, @Valid LikeRequestDto likeRequestDto) {
    User user = findUserById(userId);

    Like like = likeRepository.findByUserAndTargetIdAndTargetType(user,
            likeRequestDto.getTargetId(), likeRequestDto.getTargetType())
        .orElseThrow(() -> new LikeNotFoundException("userId: " + userId));

    likeRepository.delete(like);
  }

  @Transactional(readOnly = true)
  public int getLikeCount(Long targetId, TargetType targetType) {
    return likeRepository.countLikeByTargetIdAndTargetType(targetId, targetType);
  }

  private User findUserById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

}
