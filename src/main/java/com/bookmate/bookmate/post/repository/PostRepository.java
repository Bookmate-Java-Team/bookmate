package com.bookmate.bookmate.post.repository;

import com.bookmate.bookmate.post.entity.Post;
import com.bookmate.bookmate.post.entity.enums.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  Page<Post> findAllByCategory(PostCategory category, Pageable pageable);

}
