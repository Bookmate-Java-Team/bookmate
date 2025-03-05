package com.bookmate.bookmate.book.repository;

import com.bookmate.bookmate.book.entity.UserBookRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBookRecordsRepository extends JpaRepository<UserBookRecords, Long> {

}
