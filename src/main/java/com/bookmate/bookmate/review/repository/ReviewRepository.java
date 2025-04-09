package com.bookmate.bookmate.review.repository;

import com.bookmate.bookmate.book.entity.UserBookRecord;
import com.bookmate.bookmate.review.entity.Review;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

  Optional<Review> findByUserBookRecord(UserBookRecord userBookRecord);

}
