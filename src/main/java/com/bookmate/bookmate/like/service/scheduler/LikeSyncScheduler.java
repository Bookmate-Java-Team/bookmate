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

    Set<String> likeKeys = redisTemplate.keys("likes:*");
    Set<String> cancelKeys = redisTemplate.keys("likes_cancel:*");

    ArrayList<String> keysToDelete = new ArrayList<>();

    // Redis에서 좋아요 기록 처리
    if (likeKeys != null && !likeKeys.isEmpty()) {

      for (String key : likeKeys) {
        TargetType targetType = TargetType.valueOf(key.split(":")[1].toUpperCase());
        Long targetId = Long.valueOf(key.split(":")[2]);
        System.out.println(targetType + " " + targetId);

        Set<String> likedUsers = redisLikeService.getLikedUsers(targetType, targetId);
        List<Like> newLikes = likedUsers.stream()
            .map(userId -> Like.builder()
                .user(findUserById(Long.valueOf(userId)))
                .targetType(targetType)
                .targetId(targetId)
                .build())
            .collect(Collectors.toList());

        likeRepository.saveAll(newLikes);

        // 게시물의 totalLikes 업데이트
        if (targetType.equals(TargetType.POST)) {
          Post post = findPostById(targetId);
          post.addTotalLikes(likedUsers.size());
        }

        // Redis에서 처리된 좋아요 키 삭제
        keysToDelete.add(key);
      }
    }

    // Redis에서 취소된 좋아요 기록 처리
    if (cancelKeys != null && !cancelKeys.isEmpty()) {

      for (String cancelKey : cancelKeys) {
        TargetType targetType = TargetType.valueOf(cancelKey.split(":")[1].toUpperCase());
        Long targetId = Long.valueOf(cancelKey.split(":")[2]);

        Set<String> likedCancelUsers = redisLikeService.getCancelLikedUsers(targetType, targetId);
        likedCancelUsers.forEach(userId -> likeRepository.deleteAllByUserAndTargetTypeAndTargetId(
            findUserById(Long.valueOf(userId)), targetType, targetId));

        // 게시물의 totalLikes 업데이트 (취소된 좋아요)
        if (targetType.equals(TargetType.POST)) {
          Post post = findPostById(targetId);
          post.decreaseTotalLikes(likedCancelUsers.size());
        }

        // Redis에서 처리된 취소된 좋아요 키 삭제
        keysToDelete.add(cancelKey);
      }
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
