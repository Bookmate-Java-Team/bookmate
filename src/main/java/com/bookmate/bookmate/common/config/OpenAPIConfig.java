package com.bookmate.bookmate.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

  @Bean
  public OpenAPI springOpenAPI() {

    return new OpenAPI()
        .addSecurityItem(new SecurityRequirement().addList("JWT"))
        .components(new Components()
            .addSecuritySchemes("JWT",
                new SecurityScheme()
                    .name("JWT")
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .in(SecurityScheme.In.HEADER)
                    .description("로그인 후 발급받은 Access Token을 입력하세요.")))
        .info(new Info()
            .title("BookMate API 명세서")
            .version("1.0")
            .description("BookMate는 독서 경험을 공유하는 책 리뷰 커뮤니티 서비스입니다. " +
                "사용자는 책 리뷰를 작성하고, 댓글 및 좋아요를 통해 의견을 나눌 수 있습니다. " +
                    "또한, 독서 모임을 개설하고 참여할 수 있으며, 알라딘 API를 활용한 도서 검색 기능을 제공합니다. " +
                    "API는 JWT 기반 인증을 사용하며, Redis를 활용한 성능 최적화를 적용하였습니다."));
  }

  @Bean
  public GroupedOpenApi userAPI() {
    return GroupedOpenApi.builder()
        .group("users")
        .pathsToMatch("/api/users/**")
        .build();
  }


  @Bean
  public GroupedOpenApi bookAPI() {
    return GroupedOpenApi.builder()
        .group("books")
        .pathsToMatch("/api/books/**")
        .build();
  }

  @Bean
  public GroupedOpenApi commentAPI() {
    return GroupedOpenApi.builder()
        .group("comments")
        .pathsToMatch("/api/comments/**", "/api/posts/{postsId}/comments")
        .build();
  }

  @Bean
  public GroupedOpenApi likeAPI() {
    return GroupedOpenApi.builder()
        .group("likes")
        .pathsToMatch("/api/likes/{targetType}/{targetId}/**")
        .build();
  }

  @Bean
  public GroupedOpenApi postAPI() {
    return GroupedOpenApi.builder()
        .group("posts")
        .pathsToMatch("/api/posts/**", "/api/categories/{categoryId}/posts")
        .build();
  }

  @Bean
  public GroupedOpenApi reviewAPI() {
    return GroupedOpenApi.builder()
        .group("reviews")
        .pathsToMatch("/api/reviews/**", "/api/books/{bookId}/reviews")
        .build();
  }

  @Bean
  public GroupedOpenApi openaiAPI() {
    return GroupedOpenApi.builder()
        .group("openai")
        .pathsToMatch("/api/openai/**")
        .build();
  }

  @Bean
  public GroupedOpenApi readingClubAPI() {
    return GroupedOpenApi.builder()
        .group("readingClub")
        .pathsToMatch("/api/reading-clubs/**")
        .build();
  }


}
