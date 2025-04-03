package com.bookmate.bookmate.readingclub.dto.response;

import com.bookmate.bookmate.readingclub.entity.ReadingClub;
import io.swagger.v3.oas.annotations.media.Schema;
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

  @Schema(description = "독서모임 ID", example = "1")
  private Long id;

  @Schema(description = "독서모임 제목", example = "철학 읽는 밤")
  private String title;

  @Schema(description = "독서모임 설명", example = "함께 철학 책을 읽고 토론하는 모임입니다.")
  private String description;

  @Schema(description = "최대 참가 인원", example = "10")
  private int maxParticipants;

  @Schema(description = "모임장 ID", example = "5")
  private Long hostId;

  @Schema(description = "모임장 닉네임", example = "책읽는밤")
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
