package com.bookmate.bookmate.review.repository;

import com.bookmate.bookmate.book.entity.Book;
import com.bookmate.bookmate.book.entity.UserBookRecord;
import com.bookmate.bookmate.review.entity.Review;
import com.bookmate.bookmate.user.entity.User;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

  Optional<Review> findByUserBookRecord(UserBookRecord userBookRecord);

//  List<Review> findAllByIsbn(String isbn);
//
//  boolean existsByIsbnAndUser(@NotNull String isbn, User user);
}
