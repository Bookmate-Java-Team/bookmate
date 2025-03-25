package com.bookmate.bookmate.like.repository;

import com.bookmate.bookmate.like.entity.Like;
import com.bookmate.bookmate.like.entity.enums.TargetType;
import com.bookmate.bookmate.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

  int countLikeByTargetIdAndTargetType(Long targetId, TargetType targetType);

  boolean existsByUserAndTargetTypeAndTargetId(User user, TargetType targetType, Long targetId);

  @Modifying
  @Query("DELETE FROM Like l WHERE l.user = :user AND l.targetType = :targetType AND l.targetId = :targetId")
  void deleteAllByUserAndTargetTypeAndTargetId(
      @Param("user") User userById,
      @Param("targetType") TargetType targetType,
      @Param("targetId") Long targetId
  );
}
