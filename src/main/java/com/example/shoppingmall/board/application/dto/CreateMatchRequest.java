package com.example.shoppingmall.board.application.dto;

import com.example.shoppingmall.auth.domain.member.Member;
import com.example.shoppingmall.board.domain.match.Match;
import com.example.shoppingmall.board.domain.type.MatchCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CreateMatchRequest {

  @JsonProperty("category")  // 명시적으로 매핑
  private MatchCategory category;

  @JsonProperty("title")
  private String title;

  @JsonProperty("content")
  private String content;

  @JsonProperty("location")
  private String location;

  @JsonProperty("matchDate")
  private LocalDateTime matchDate;

  @JsonProperty("maxPlayers")
  private Integer maxPlayers;

  @Builder
  public CreateMatchRequest(MatchCategory category, String title, String content,
      String location, LocalDateTime matchDate, Integer maxPlayers) {
    this.category = category;
    this.title = title;
    this.content = content;
    this.location = location;
    this.matchDate = matchDate;
    this.maxPlayers = maxPlayers;
  }

  public CreateMatchRequest(MatchCategory category, String title, String content) {
    this.category = category;
    this.title = title;
    this.content = content;
  }

  public Match toMatchingPost(Member member) {
      return Match.createMatchingPost(
          member,
          this.title,
          this.content,
          this.location,
          this.matchDate,
          this.maxPlayers
      );
  }

  public boolean isMatchingPost() {
    return this.category == MatchCategory.MATCHING;
  }

  public static CreateMatchRequest createFreePost(String title, String content) {
    return new CreateMatchRequest(MatchCategory.FREE, title, content);
  }

  public static CreateMatchRequest createNotice(String title, String content) {
    return new CreateMatchRequest(MatchCategory.NOTICE, title, content);
  }

  public static CreateMatchRequest createMatchingPost(String title, String content,
      String location, LocalDateTime matchDate,
      Integer maxPlayers) {
    return CreateMatchRequest.builder()
        .category(MatchCategory.MATCHING)
        .title(title)
        .content(content)
        .location(location)
        .matchDate(matchDate)
        .maxPlayers(maxPlayers)
        .build();
  }
}