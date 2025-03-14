package com.bookmate.bookmate.readingclub.repository;

import com.bookmate.bookmate.readingclub.entity.ReadingClub;
import com.bookmate.bookmate.readingclub.entity.ReadingClubUser;
import com.bookmate.bookmate.readingclub.entity.enums.ReadingClubUserStatus;
import com.bookmate.bookmate.user.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadingClubUserRepository extends JpaRepository<ReadingClubUser, Long> {

  // 모임 가입자 or 신청자 수 조회
  @Query("SELECT COUNT(rcu) FROM ReadingClubUser rcu WHERE rcu.readingClub = :readingClub AND rcu.status = :status")
  long countByReadingClubAndStatus(ReadingClub readingClub, ReadingClubUserStatus status);

  // 특정 유저 & 특정 모임에 대한 참가 정보 조회
  Optional<ReadingClubUser> findByReadingClubAndUser(ReadingClub readingClub, User user);

  // 특정 유저가 가입한 모든 모임 목록 조회 (페이징)
  @Query("""
        SELECT rcu.readingClub 
        FROM ReadingClubUser rcu 
        WHERE rcu.user = :user
        """)
  Page<ReadingClub> findReadingClubsByUser(User user, Pageable pageable);

  // 특정 모임에서 상태별 유저 목록 (페이징)
  @Query("""
        SELECT rcu
          FROM ReadingClubUser rcu
         WHERE rcu.readingClub = :club
           AND rcu.status = :status
        """)
  Page<ReadingClubUser> findByReadingClubAndStatus(ReadingClub club, ReadingClubUserStatus status, Pageable pageable);
}
