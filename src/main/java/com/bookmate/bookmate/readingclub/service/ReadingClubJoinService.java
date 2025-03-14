package com.bookmate.bookmate.readingclub.service;

public interface ReadingClubJoinService {

  // 독서모임 가입 신청
  void applyForReadingClub(Long userId, Long readingClubId);

  // 독서모임 가입 승인 (host만 가능, 정원 체크)
  void acceptUser(Long hostUserId, Long readingClubId, Long targetUserId);

  // 독서모임 가입 거절
  void rejectUser(Long hostUserId, Long clubId, Long targetUserId);

  // 독서모임 탈퇴
  void leaveReadingClub(Long userId, Long readingClubId);
}
