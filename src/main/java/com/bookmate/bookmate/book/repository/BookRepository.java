package com.bookmate.bookmate.book.repository;

import com.bookmate.bookmate.book.entity.Book;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

  Optional<Book> findByIsbn(@NotBlank String isbn);
}
