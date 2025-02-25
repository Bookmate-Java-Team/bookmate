package com.bookmate.bookmate.book.controller;

import com.bookmate.bookmate.book.dto.AladinApiResponseDto;
import com.bookmate.bookmate.book.service.AladinApiService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/community/books")
@RequiredArgsConstructor
public class BookController {
  private final AladinApiService aladinApiService;

  @GetMapping("/search")
  public String searchBooks(@RequestParam String query) {
    List<AladinApiResponseDto> itemList = aladinApiService.searchBooks(query);
    return itemList.toString();
  }
}