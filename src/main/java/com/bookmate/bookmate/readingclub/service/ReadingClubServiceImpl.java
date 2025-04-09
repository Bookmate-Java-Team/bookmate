package com.bookmate.bookmate.readingclub.service;

import com.bookmate.bookmate.chat.dto.request.CreateChatRoomRequestDto;
import com.bookmate.bookmate.chat.service.ChatService;
import com.bookmate.bookmate.common.error.exception.ErrorCode;
import com.bookmate.bookmate.readingclub.dto.request.ReadingClubRequestDto;
import com.bookmate.bookmate.readingclub.dto.response.ReadingClubResponseDto;
import com.bookmate.bookmate.readingclub.dto.response.ReadingClubUserResponseDto;
import com.bookmate.bookmate.readingclub.entity.ReadingClub;
import com.bookmate.bookmate.readingclub.entity.ReadingClubUser;
import com.bookmate.bookmate.readingclub.entity.enums.ReadingClubUserStatus;
import com.bookmate.bookmate.readingclub.exception.ReadingClubForbiddenException;
import com.bookmate.bookmate.readingclub.exception.ReadingClubNotFoundException;
import com.bookmate.bookmate.readingclub.repository.ReadingClubRepository;
import com.bookmate.bookmate.readingclub.repository.ReadingClubUserRepository;
import com.bookmate.bookmate.user.dto.response.UserResponseDto;
import com.bookmate.bookmate.user.entity.User;
import com.bookmate.bookmate.user.exception.UserNotFoundException;
import com.bookmate.bookmate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReadingClubServiceImpl implements ReadingClubService {

  private final ReadingClubRepository readingClubRepository;
  private final ReadingClubUserRepository readingClubUserRepository;
  private final UserRepository userRepository;
  private final ChatService chatService;

  // 독서모임 생성
  @Override
  public ReadingClubResponseDto createReadingClub(Long hostUserId, ReadingClubRequestDto requestDto) {
    User host = findUserById(hostUserId);

    ReadingClub readingClub = ReadingClub.builder()
        .host(host)
        .title(requestDto.getTitle())
        .description(requestDto.getDescription())
        .maxParticipants(requestDto.getMaxParticipants())
        .build();

    ReadingClub saved = readingClubRepository.save(readingClub);

    // 호스트 본인을 독서모임에 자동 가입
    ReadingClubUser hostJoin = ReadingClubUser.builder()
        .readingClub(saved)
        .user(host)
        .status(ReadingClubUserStatus.ACCEPTED)
        .build();
    readingClubUserRepository.save(hostJoin);

    // 그룹채팅방 자동 생성
    CreateChatRoomRequestDto createChatRoomRequestDto = CreateChatRoomRequestDto.builder()
        .readingClubId(saved.getId())
        .build();
    chatService.createChatRoom(createChatRoomRequestDto, host);

    return ReadingClubResponseDto.fromEntity(saved);
  }

  // 독서모임 수정
  @Override
  public ReadingClubResponseDto updateReadingClub(Long hostUserId, Long readingClubId, ReadingClubRequestDto requestDto) {
    ReadingClub club = findReadingClubById(readingClubId);

    if (!club.getHost().getId().equals(hostUserId)) {
      throw new ReadingClubForbiddenException(ErrorCode.READING_CLUB_UPDATE_DENIED);
    }

    club.updateReadingClub(requestDto.getTitle(), requestDto.getDescription(), requestDto.getMaxParticipants());
    return ReadingClubResponseDto.fromEntity(club);
  }

  // 독서모임 삭제
  @Override
  public void deleteReadingClub(Long hostUserId, Long readingClubId) {
    ReadingClub club = findReadingClubById(readingClubId);

    if (!club.getHost().getId().equals(hostUserId)) {
      throw new ReadingClubForbiddenException(ErrorCode.READING_CLUB_DELETE_DENIED);
    }

    // 해당 ChatRoom 삭제
    chatService.deleteChatRoomByReadingClub(readingClubId);

    readingClubUserRepository.deleteByReadingClub(club);
    readingClubRepository.delete(club);
  }

  // 특정 유저가 가입한 독서모임 목록 조회 (페이징)
  @Override
  @Transactional(readOnly = true)
  public Page<ReadingClubResponseDto> getReadingClubsByUser(Long userId, Pageable pageable) {
    User user = findUserById(userId);
    return readingClubUserRepository.findReadingClubsByUser(user, pageable)
        .map(ReadingClubResponseDto::fromEntity);
  }

  // 전체 독서모임 목록 조회  (페이징)
  @Override
  @Transactional(readOnly = true)
  public Page<ReadingClubResponseDto> getAllReadingClubs(Pageable pageable) {
    return readingClubRepository.findAll(pageable)
        .map(ReadingClubResponseDto::fromEntity);
  }

  // 특정 독서모임 조회
  @Override
  @Transactional(readOnly = true)
  public ReadingClubResponseDto getReadingClub(Long readingClubId) {
    ReadingClub club = findReadingClubById(readingClubId);
    return ReadingClubResponseDto.fromEntity(club);
  }

  // 특정 독서모임 + 특정 status에 해당하는 사용자 목록 (페이징)
  @Override
  @Transactional(readOnly = true)
  public Page<ReadingClubUserResponseDto> getReadingClubUsersByStatus(Long clubId, ReadingClubUserStatus status, Pageable pageable) {
    ReadingClub club = findReadingClubById(clubId);
    return readingClubUserRepository.findByReadingClubAndStatus(club, status, pageable)
        .map(ReadingClubUserResponseDto::fromEntity);
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
