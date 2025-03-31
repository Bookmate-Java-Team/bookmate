package com.bookmate.bookmate.comment.repository;

import com.bookmate.bookmate.comment.entity.Comment;
import com.bookmate.bookmate.post.entity.Post;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> findAllByParent(Comment parentComment);

  Page<Comment> findAllByPostAndParentIsNullAndDeleteAtIsNull(Post post, Pageable pageable);

  @Modifying
  @Query("DELETE FROM Comment c WHERE c.id = :parentId")
  void deleteParent(Long parentId);

  @Modifying
  @Query("DELETE FROM Comment c WHERE c.parent.id = :parentId")
  void deleteChildren(Long parentId);
}
