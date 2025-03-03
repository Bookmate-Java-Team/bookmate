package com.bookmate.bookmate.book.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AladinApiResponseDto {
    private String title;
    private String author;
    private String pubDate;
    private String description;
    private String isbn13;
    private String cover;
    private String publisher;
    private int priceStandard;

}
