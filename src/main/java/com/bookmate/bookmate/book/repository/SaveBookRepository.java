package com.bookmate.bookmate.book.repository;

import com.bookmate.bookmate.book.entity.SaveBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaveBookRepository extends JpaRepository<SaveBook, Long> {

}
