package com.bookmate.bookmate.book.controller;

import com.bookmate.bookmate.book.dto.AladinApiResponseDto;
import com.bookmate.bookmate.book.dto.BookRequestDto;
import com.bookmate.bookmate.book.dto.BookResponseDto;
import com.bookmate.bookmate.book.service.AladinApiService;
import com.bookmate.bookmate.book.service.BookService;
import com.bookmate.bookmate.common.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Book API", description = "도서 관련 API")
public class BookController {

  private final AladinApiService aladinApiService;
  private final BookService bookService;

  @GetMapping("/search")
  @Operation(
      summary = "도서 검색",
      description = "알라딘 Open API를 활용하여 도서를 검색합니다. \n\n" +
          "- `type=Bestseller`를 전달하면 베스트셀러 목록이 조회됩니다.\n" +
          "- `type=Title`을 전달하면 제목 기반 검색을 수행합니다."
  )
  @ApiResponse(responseCode = "200", description = "검색 성공",
      content = @Content(schema = @Schema(implementation = AladinApiResponseDto.class)))
  public ResponseEntity<List<AladinApiResponseDto>> searchBooks(
      @Parameter(description = "검색어", example = "스프링 부트")
      @RequestParam String query,

      @Parameter(description = "검색 유형 (Title: 제목 검색, Bestseller: 베스트셀러 조회)",
          example = "Title",
          required = false,
          schema = @Schema(defaultValue = "Title"))
      @RequestParam(value = "type", defaultValue = "Title") String type,

      @Parameter(description = "조회 시작 페이지 (1부터 시작)", example = "1")
      @RequestParam String startPage) {
    List<AladinApiResponseDto> itemList = aladinApiService.searchBooks(query, type, startPage);
    return ResponseEntity.ok(itemList);
  }

  @PostMapping
  @Operation(
      summary = "도서 저장",
      description = "회원이 읽은 도서를 저장합니다. 이후 리뷰를 작성하기 위해 사용됩니다."
  )
  @ApiResponse(responseCode = "200", description = "도서 저장 성공",
      content = @Content(schema = @Schema(implementation = BookResponseDto.class)))
  public ResponseEntity<BookResponseDto> userSavedBook(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Valid BookRequestDto bookRequestDto
  ) {
    Long userId = userDetails.getUser().getId();
    return ResponseEntity.ok(BookResponseDto.toDto(bookService.userSavedBook(userId, bookRequestDto)));
  }

}