package com.bookmate.bookmate.readingclub.service;

import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.readingclub.entity.ReadingClub;
import com.bookmate.bookmate.readingclub.entity.ReadingClubUser;
import com.bookmate.bookmate.readingclub.entity.enums.ReadingClubUserStatus;
import com.bookmate.bookmate.readingclub.exception.ReadingClubFullException;
import com.bookmate.bookmate.readingclub.exception.ReadingClubForbiddenException;
import com.bookmate.bookmate.readingclub.exception.ReadingClubNotFoundException;
import com.bookmate.bookmate.readingclub.repository.ReadingClubRepository;
import com.bookmate.bookmate.readingclub.repository.ReadingClubUserRepository;
import com.bookmate.bookmate.user.entity.User;
import com.bookmate.bookmate.user.exception.UserNotFoundException;
import com.bookmate.bookmate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReadingClubJoinServiceImpl implements ReadingClubJoinService {

  private final ReadingClubRepository readingClubRepository;
  private final ReadingClubUserRepository readingClubUserRepository;
  private final UserRepository userRepository;

  // 독서모임 가입 신청
  @Override
  public void applyForReadingClub(Long userId, Long readingClubId) {
    User user = findUserById(userId);
    ReadingClub club = findReadingClubById(readingClubId);

    if (readingClubUserRepository.findByReadingClubAndUser(club, user).isPresent()) {
      throw new ReadingClubForbiddenException(ErrorCode.READING_CLUB_DUPLICATE_JOIN);
    }

    long acceptedCount = readingClubUserRepository.countByReadingClubAndStatus(
        club, ReadingClubUserStatus.ACCEPTED);
    if (acceptedCount >= club.getMaxParticipants()) {
      throw new ReadingClubFullException();
    }

    ReadingClubUser record = ReadingClubUser.builder()
        .readingClub(club)
        .user(user)
        .build();
    readingClubUserRepository.save(record);
  }

  // 독서모임 가입 승인 (host만 가능, 정원 체크)
  @Override
  public void acceptUser(Long hostUserId, Long readingClubId, Long targetUserId) {
    ReadingClub club = findReadingClubById(readingClubId);
    if (!club.getHost().getId().equals(hostUserId)) {
      throw new ReadingClubForbiddenException(ErrorCode.READING_CLUB_UPDATE_DENIED);
    }

    User targetUser = findUserById(targetUserId);
    ReadingClubUser rcu = readingClubUserRepository
        .findByReadingClubAndUser(club, targetUser)
        .orElseThrow(() -> new ReadingClubForbiddenException(ErrorCode.READING_CLUB_NOT_JOINED));

    if (rcu.getStatus() != ReadingClubUserStatus.PENDING) {
      throw new ReadingClubForbiddenException(ErrorCode.READING_CLUB_INVALID_STATUS);
    }

    long acceptedCount = readingClubUserRepository.countByReadingClubAndStatus(club, ReadingClubUserStatus.ACCEPTED);
    if (acceptedCount >= club.getMaxParticipants()) {
      throw new ReadingClubFullException();
    }

    rcu.accept();
  }

  // 독서모임 가입 거절
  @Override
  public void rejectUser(Long hostUserId, Long clubId, Long targetUserId) {
    ReadingClub club = findReadingClubById(clubId);
    if (!club.getHost().getId().equals(hostUserId)) {
      throw new ReadingClubForbiddenException(ErrorCode.READING_CLUB_UPDATE_DENIED);
    }

    User targetUser = findUserById(targetUserId);

    ReadingClubUser rcu = readingClubUserRepository
        .findByReadingClubAndUser(club, targetUser)
        .orElseThrow(() -> new ReadingClubForbiddenException(ErrorCode.READING_CLUB_NOT_JOINED));

    if (rcu.getStatus() != ReadingClubUserStatus.PENDING) {
      throw new ReadingClubForbiddenException(ErrorCode.READING_CLUB_INVALID_STATUS);
    }

    rcu.reject();
  }

  // 독서모임 탈퇴
  @Override
  public void leaveReadingClub(Long userId, Long readingClubId) {
    User user = findUserById(userId);
    ReadingClub club = findReadingClubById(readingClubId);

    ReadingClubUser record = readingClubUserRepository.findByReadingClubAndUser(club, user)
        .orElseThrow(() -> new ReadingClubForbiddenException(ErrorCode.READING_CLUB_NOT_JOINED));

    readingClubUserRepository.delete(record);
  }

  private User findUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
  }

  private ReadingClub findReadingClubById(Long id) {
    return readingClubRepository.findById(id)
        .orElseThrow(() -> new ReadingClubNotFoundException(id));
  }
}
