package com.example.shoppingmall.board.application.dto;

import com.example.shoppingmall.board.domain.match.Match;
import com.example.shoppingmall.board.domain.type.MatchCategory;
import com.example.shoppingmall.board.domain.type.MatchStatus;
import java.time.LocalDateTime;

public record MatchResponse(
    Long id,
    MatchCategory category,
    String title,
    String content,
    String location,
    LocalDateTime matchDate,
    Integer maxPlayers,
    MatchStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long member
) {
  public static MatchResponse from(Match match) {
    return new MatchResponse(
        match.getId(),
        match.getCategory(),
        match.getTitle(),
        match.getContent(),
        match.getLocation(),
        match.getMatchDate(),
        match.getMaxPlayers(),
        match.getStatus(),
        match.getCreatedAt(),
        match.getUpdatedAt(),
        match.getMember().getId()
    );
  }
}