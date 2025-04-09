package com.bookmate.bookmate.book.repository;

import com.bookmate.bookmate.book.entity.Book;
import com.bookmate.bookmate.book.entity.UserBookRecord;
import com.bookmate.bookmate.user.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBookRecordRepository extends JpaRepository<UserBookRecord, Long> {

  Optional<Page<UserBookRecord>> findAllByBook(Book book, Pageable pageable);

  Optional<UserBookRecord> findByUserAndBook(User user, Book book);
}
