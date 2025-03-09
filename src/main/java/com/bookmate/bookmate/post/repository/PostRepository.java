package com.bookmate.bookmate.post.repository;

import com.bookmate.bookmate.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
