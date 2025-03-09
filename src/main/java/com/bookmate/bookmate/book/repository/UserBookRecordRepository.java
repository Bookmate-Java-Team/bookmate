package com.bookmate.bookmate.book.repository;

import com.bookmate.bookmate.book.entity.Book;
import com.bookmate.bookmate.book.entity.UserBookRecord;
import com.bookmate.bookmate.user.entity.User;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBookRecordRepository extends JpaRepository<UserBookRecord, Long> {

  Optional<List<UserBookRecord>> findAllByBook(Book book);

  Optional<UserBookRecord> findByUserAndBook(User user, Book book);
}
