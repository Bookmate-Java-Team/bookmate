package com.bookmate.bookmate.readingclub.dto.response;

import com.bookmate.bookmate.readingclub.entity.ReadingClub;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 독서모임 응답 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadingClubResponseDto {
  private Long id;
  private String title;
  private String description;
  private int maxParticipants;
  private Long hostId;
  private String hostNickname;

  public static ReadingClubResponseDto fromEntity(ReadingClub readingClub) {
    return ReadingClubResponseDto.builder()
        .id(readingClub.getId())
        .title(readingClub.getTitle())
        .description(readingClub.getDescription())
        .maxParticipants(readingClub.getMaxParticipants())
        .hostId(readingClub.getHost().getId())
        .hostNickname(readingClub.getHost().getNickname())
        .build();
  }
}
