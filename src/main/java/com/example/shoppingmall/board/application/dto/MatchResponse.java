package com.example.shoppingmall.board.application.dto;

import com.example.shoppingmall.auth.application.dto.MemberResponse;
import com.example.shoppingmall.board.domain.match.Match;
import com.example.shoppingmall.board.domain.type.MatchCategory;
import com.example.shoppingmall.board.domain.type.MatchStatus;
import java.time.LocalDateTime;

public record MatchResponse(
    Long id,
    MatchCategory category,
    String title,
    String content,
    String locationArea,
    String locationDetail,
    LocalDateTime matchDate,
    Integer maxPlayers,
    Integer currentPlayers,
    String gameFormat,
    MatchStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    MemberResponse member) {
  public static MatchResponse from(Match match) {
    return new MatchResponse(
        match.getId(),
        match.getCategory(),
        match.getTitle(),
        match.getContent(),
        match.getLocationArea(),
        match.getLocationDetail(),
        match.getMatchDate(),
        match.getMaxPlayers(),
        match.getCurrentPlayers(),
        match.getGameFormat(),
        match.getStatus(),
        match.getCreatedAt(),
        match.getUpdatedAt(),
        MemberResponse.from(match.getMember())
    );
  }
}