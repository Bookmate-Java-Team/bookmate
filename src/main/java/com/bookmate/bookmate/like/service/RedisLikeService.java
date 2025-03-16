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

  public void addLike(Long userId, TargetType targetType, Long targetId) {
    String key = getKey(targetType, targetId);
    redisTemplate.opsForSet().add(key, userId.toString());
  }

  public void deleteLike(Long userId, TargetType targetType, Long targetId) {
    String key = getKey(targetType, targetId);
    redisTemplate.opsForSet().remove(key, userId.toString());
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
    return "likes:" + targetType.toString().toLowerCase() + ":" + targetId;
  }

}
