package com.bookmate.bookmate.main.service;


import com.bookmate.bookmate.main.dto.AladinApiResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AladinApiService {

  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Value("${aladin.api-key}")
  private String API_KEY;

  /**
   * 알라딘 api를 통해 도서를 검색합니다.
   *
   * @param query 사용자가 검색한 도서 제목(혹은 키워드)
   * @return 검색된 도서 정보가 담긴 {@link List} 객체, 각 항목은 {@link AladinApiResponseDto} 객체
   * @throws RuntimeException 알라딘 API 호출 실패 또는 처리 중 오류 발생 시
   */
  public List<AladinApiResponseDto> searchBooks(String query, String startPage) {
    try {
      String API_URL = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx";
      String url = String.format(
          "%s?ttbkey=%s&Query=%s&QueryType=Title&MaxResults=8&start=%s&output=js&Version=20131101",
          API_URL, API_KEY, query, startPage  );

      ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

      if (response.getStatusCode().is2xxSuccessful()) {
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        JsonNode itemNode = rootNode.get("item");
        List<AladinApiResponseDto> result = new ArrayList<>();
        for (JsonNode node : itemNode) {
          String title = node.get("title").asText();
          String author = node.get("author").asText();
          String pubDate = node.get("pubDate").asText();
          String description = node.get("description").asText();
          String isbn13 = node.get("isbn13").asText();
          String cover = node.get("cover").asText();
          String publisher = node.get("publisher").asText();
          int priceStandard = node.get("priceStandard").asInt();

          result.add(
              new AladinApiResponseDto(title, author, pubDate, description, isbn13, cover, publisher,
                  priceStandard));
        }
        return result;
      } else {
        throw new RuntimeException("알라딘 API 호출 실패: " + response.getStatusCode());
      }
    } catch (Exception e) {
      throw new RuntimeException("알라딘 API 호출 중 오류 발생", e);
    }
  }
}
