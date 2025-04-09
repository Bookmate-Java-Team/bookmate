package com.bookmate.bookmate.book.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "알라딘 API 도서 검색 응답")
public class AladinApiResponseDto {

    @Schema(description = "도서 제목", example = "스프링 부트와 AWS로 혼자 구현하는 웹 서비스")
    private String title;

    @Schema(description = "저자", example = "이동욱")
    private String author;

    @Schema(description = "출판일", example = "2020-06-30")
    private String pubDate;

    @Schema(description = "도서 설명", example = "스프링 부트와 AWS를 활용한 실습 중심의 웹 서비스 개발")
    private String description;

    @Schema(description = "ISBN13 코드", example = "9788965402602")
    private String isbn13;

    @Schema(description = "도서 표지 이미지 URL", example = "https://image.aladin.co.kr/product/2602/cover.jpg")
    private String cover;

    @Schema(description = "출판사", example = "프리렉")
    private String publisher;

    @Schema(description = "정가", example = "25000")
    private int priceStandard;
}
