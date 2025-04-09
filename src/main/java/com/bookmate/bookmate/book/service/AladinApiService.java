package com.bookmate.bookmate.book.service;


import com.bookmate.bookmate.book.dto.AladinApiResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
   * 알라딘 API를 통해 도서를 검색합니다.
   *
   * @param query     사용자가 검색한 도서 제목(혹은 키워드)
   * @param type      사용자가 원하는 조회 방식(default = Title, Bestseller)
   * @param startPage 검색 시작 페이지
   * @return 검색된 도서 목록 ({@link AladinApiResponseDto} 리스트)
   */
  public List<AladinApiResponseDto> searchBooks(String query, String type, String startPage) {
    String API_URL = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx";
    String url;
    if (type.equals("Bestseller")) {
      url = String.format(
          "https://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=%s&QueryType=Bestseller&MaxResults=8&start=%s&SearchTarget=Book&output=js&Version=20131101",
          API_KEY, startPage);
    } else {
      url = String.format(
          "%s?ttbkey=%s&Query=%s&QueryType=Title&MaxResults=8&start=%s&output=js&Version=20131101",
          API_URL, API_KEY, query, startPage);
    }
    return callAladinApi(url);
  }

  /**
   * 알라딘 API를 호출하여 응답을 파싱합니다.
   *
   * @param url API 요청 URL
   * @return 파싱된 도서 목록 ({@link AladinApiResponseDto} 리스트)
   */
  private List<AladinApiResponseDto> callAladinApi(String url) {
    try {
      ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

      if (response.getStatusCode().is2xxSuccessful()) {
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        JsonNode itemNode = rootNode.get("item");

        return StreamSupport.stream(itemNode.spliterator(), false)
            .map(node -> new AladinApiResponseDto(
                node.get("title").asText(),
                node.get("author").asText(),
                node.get("pubDate").asText(),
                node.get("description").asText(),
                node.get("isbn13").asText(),
                node.get("cover").asText(),
                node.get("publisher").asText(),
                node.get("priceStandard").asInt()))
            .collect(Collectors.toList());

      } else {
        throw new RuntimeException("알라딘 API 호출 실패: " + response.getStatusCode());
      }
    } catch (Exception e) {
      throw new RuntimeException("알라딘 API 호출 중 오류 발생", e);
    }
  }
}
