package com.bookmate.bookmate.main.controller;

import com.bookmate.bookmate.main.dto.AladinApiResponseDto;
import com.bookmate.bookmate.main.service.AladinApiService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class BookController {
  private final AladinApiService aladinApiService;

  @GetMapping("/search")
  public ResponseEntity<List<AladinApiResponseDto>> searchBooks(@RequestParam String query, @RequestParam String startPage) {
    List<AladinApiResponseDto> itemList = aladinApiService.searchBooks(query, startPage);
    return ResponseEntity.ok(itemList);
  }

  @GetMapping("/search/bestseller")
  public ResponseEntity<List<AladinApiResponseDto>> searchBestSellerBooks(@RequestParam String startPage) {
    List<AladinApiResponseDto> itemList = aladinApiService.searchBestSellerBooks(startPage);
    return ResponseEntity.ok(itemList);
  }

}