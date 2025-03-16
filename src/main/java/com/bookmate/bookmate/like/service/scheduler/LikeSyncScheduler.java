package com.bookmate.bookmate.like.service.scheduler;


import com.bookmate.bookmate.like.entity.Like;
import com.bookmate.bookmate.like.entity.enums.TargetType;
import com.bookmate.bookmate.like.repository.LikeRepository;
import com.bookmate.bookmate.like.service.RedisLikeService;
import com.bookmate.bookmate.post.entity.Post;
import com.bookmate.bookmate.post.exception.PostNotFoundException;
import com.bookmate.bookmate.post.repository.PostRepository;
import com.bookmate.bookmate.user.entity.User;
import com.bookmate.bookmate.user.exception.UserNotFoundException;
import com.bookmate.bookmate.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeSyncScheduler {

  private final RedisLikeService redisLikeService;
  private final LikeRepository likeRepository;
  private final PostRepository postRepository;
  private final StringRedisTemplate redisTemplate;
  private final UserRepository userRepository;

  @Scheduled(fixedRate = 60000)
  @Transactional
  public void syncLikesToDatabase() {
    log.info("Start sync likes");

    Set<String> keys = redisTemplate.keys("likes:*");
    if (keys == null || keys.isEmpty()) {
      return;
    }

    List<String> keysToDelete = new ArrayList<>();

    for (String key : keys) {
      TargetType targetType = TargetType.valueOf(key.split(":")[1].toUpperCase());
      Long targetId = Long.valueOf(key.split(":")[2]);

      Set<String> likedUsers = redisLikeService.getLikedUsers(targetType, targetId);

      List<Like> newLikes = likedUsers.stream()
          .map(userId -> Like.builder()
              .user(findUserById(Long.valueOf(userId)))
              .targetType(targetType)
              .targetId(targetId)
              .build())
          .collect(Collectors.toList());

      likeRepository.saveAll(newLikes);

      // post의 totalLikes update
      if (targetType.equals(TargetType.POST)) {
        Post post = findPostById(targetId);
        post.addTotalLikes(likedUsers.size());
      }

      keysToDelete.add(key);
    }

    // 데이터 손실을 방지하기 위해 트랜잭션이 성공한 후 일괄 삭제
    keysToDelete.forEach(key -> redisTemplate.delete(key));
    log.info("Finish sync likes");
  }

  private User findUserById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  private Post findPostById(Long id) {
    return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
  }

}
