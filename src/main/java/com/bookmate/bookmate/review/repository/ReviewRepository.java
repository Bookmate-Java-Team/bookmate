package com.bookmate.bookmate.review.repository;

import com.bookmate.bookmate.review.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

//  List<Review> findAllByDeletedAtIsNull(String isbn);

  List<Review> findAllByIsbn(String isbn);
}
