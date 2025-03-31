package com.bookmate.bookmate.book.controller;

import com.bookmate.bookmate.book.dto.AladinApiResponseDto;
import com.bookmate.bookmate.book.dto.BookRequestDto;
import com.bookmate.bookmate.book.dto.BookResponseDto;
import com.bookmate.bookmate.book.service.AladinApiService;
import com.bookmate.bookmate.book.service.BookService;
import com.bookmate.bookmate.common.security.CustomUserDetails;
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
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
  private final AladinApiService aladinApiService;
  private final BookService bookService;

  @GetMapping("/search")
  public ResponseEntity<List<AladinApiResponseDto>> searchBooks(
      @RequestParam String query,
      @RequestParam(value = "type", defaultValue = "Title") String type,
      @RequestParam String startPage) {
    List<AladinApiResponseDto> itemList = aladinApiService.searchBooks(query, type, startPage);
    return ResponseEntity.ok(itemList);
  }

  @PostMapping
  public ResponseEntity<BookResponseDto> userSavedBook(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid BookRequestDto bookRequestDto) {
    Long userId = userDetails.getUser().getId();
    return ResponseEntity.ok(BookResponseDto.toDto(bookService.userSavedBook(userId, bookRequestDto)));
  }

}