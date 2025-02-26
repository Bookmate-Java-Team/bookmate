package com.bookmate.bookmate.book.controller;

import com.bookmate.bookmate.book.dto.AladinApiResponseDto;
import com.bookmate.bookmate.book.service.AladinApiService;
import com.bookmate.bookmate.book.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/community/books")
@RequiredArgsConstructor
public class BookController {
  private final AladinApiService aladinApiService;
  private final BookService bookService;

  @GetMapping("/search")
  public ResponseEntity<List<AladinApiResponseDto>> searchBooks(@RequestParam String query, @RequestParam String startPage) {
    List<AladinApiResponseDto> itemList = aladinApiService.searchBooks(query, startPage);
    return ResponseEntity.ok(itemList);
  }

  @PostMapping("/api/save")
  public ResponseEntity<String> saveBook(@RequestParam AladinApiResponseDto request) {
    bookService.saveBook(request);
    return ResponseEntity.ok("도서가 저장되었습니다.");
  }


}