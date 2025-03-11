package com.bookmate.bookmate.like.repository;

import com.bookmate.bookmate.like.entity.Like;
import com.bookmate.bookmate.like.entity.enums.TargetType;
import com.bookmate.bookmate.user.entity.User;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

  int countLikeByTargetIdAndTargetType(Long targetId, TargetType targetType);

  Optional<Like> findByUserAndTargetIdAndTargetType(User user, @NotNull Long targetId, @NotNull TargetType targetType);

  boolean existsByUserAndTargetIdAndTargetType(User user, @NotNull Long targetId, @NotNull TargetType targetType);
}
