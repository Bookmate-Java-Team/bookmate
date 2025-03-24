package com.bookmate.bookmate.like.service;

import com.bookmate.bookmate.like.entity.enums.TargetType;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisLikeService {

  private final StringRedisTemplate redisTemplate;
  public static final String LIKE_KEY_PATTERN = "likes:%s:%d";
  public static final String LIKE_COUNT_KEY_PATTERN = "likes_count:%s:%d";
  public static final String LIKE_CANCEL_PATTERN = "likes_cancel:%s:%d";

  public void addLike(Long userId, TargetType targetType, Long targetId) {
    String key = getKey(targetType, targetId);
    redisTemplate.opsForSet().add(key, userId.toString());
  }

  public void deleteLike(Long userId, TargetType targetType, Long targetId) {
    String key = getKey(targetType, targetId);
    Long removed = redisTemplate.opsForSet().remove(key, userId.toString());

    // Redis에 좋아요 데이터가 없었고, MySQL에만 존재하는 경우
    if (removed == 0) {
      key = getCancelKey(targetType, targetId);
      redisTemplate.opsForSet().add(key, userId.toString());
    }
  }

  public boolean hasLiked(Long userId, TargetType targetType, Long targetId) {
    String key = getKey(targetType, targetId);
    return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, userId.toString()));
  }

  public Set<String> getLikedUsers(TargetType targetType, Long targetId) {
    String key = getKey(targetType, targetId);
    return redisTemplate.opsForSet().members(key);
  }


  public String getKey(TargetType targetType, Long targetId) {
    return String.format(LIKE_KEY_PATTERN, targetType.toString().toLowerCase(), targetId);
  }

  public String getCountKey(TargetType targetType, Long targetId) {
    return String.format(LIKE_COUNT_KEY_PATTERN, targetType.toString().toLowerCase(), targetId);
  }

  public String getCancelKey(TargetType targetType, Long targetId) {
    return String.format(LIKE_CANCEL_PATTERN, targetType.toString().toLowerCase(), targetId);
  }

  public Set<String> getCancelLikedUsers(TargetType targetType, Long targetId) {
    String key = getCancelKey(targetType, targetId);
    return redisTemplate.opsForSet().members(key);
  }
}
