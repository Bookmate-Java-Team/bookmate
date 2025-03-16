package com.bookmate.bookmate.like.repository;

import com.bookmate.bookmate.like.entity.Like;
import com.bookmate.bookmate.like.entity.enums.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

  int countLikeByTargetIdAndTargetType(Long targetId, TargetType targetType);

}
