package com.bookmate.bookmate.readingclub.service;

import com.bookmate.bookmate.readingclub.dto.request.ReadingClubRequestDto;
import com.bookmate.bookmate.readingclub.dto.response.ReadingClubResponseDto;
import com.bookmate.bookmate.readingclub.dto.response.ReadingClubUserResponseDto;
import com.bookmate.bookmate.readingclub.entity.enums.ReadingClubUserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReadingClubService {

  // 독서모임 생성
  ReadingClubResponseDto createReadingClub(Long hostUserId, ReadingClubRequestDto requestDto);

  // 독서모임 수정
  ReadingClubResponseDto updateReadingClub(Long hostUserId, Long readingClubId, ReadingClubRequestDto requestDto);

  // 독서모임 삭제
  void deleteReadingClub(Long hostUserId, Long readingClubId);

  // 특정 유저가 가입한 독서모임 목록 조회 (페이징)
  Page<ReadingClubResponseDto> getReadingClubsByUser(Long userId, Pageable pageable);

  // 전체 독서모임 목록 조회  (페이징)
  Page<ReadingClubResponseDto> getAllReadingClubs(Pageable pageable);

  // 특정 독서모임 조회
  ReadingClubResponseDto getReadingClub(Long readingClubId);

  // 특정 독서모임 + 특정 status에 해당하는 사용자 목록 (페이징)
  Page<ReadingClubUserResponseDto> getReadingClubUsersByStatus(Long clubId, ReadingClubUserStatus status, Pageable pageable);
}
