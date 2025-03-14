package com.bookmate.bookmate.readingclub.repository;

import com.bookmate.bookmate.readingclub.entity.ReadingClub;
import com.bookmate.bookmate.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadingClubRepository extends JpaRepository<ReadingClub, Long> {
  Optional<ReadingClub> findByIdAndHost(Long id, User host);
}
