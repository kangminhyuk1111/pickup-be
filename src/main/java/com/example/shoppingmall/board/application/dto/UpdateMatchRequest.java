package com.example.shoppingmall.board.application.dto;

import com.example.shoppingmall.auth.domain.member.Member;
import com.example.shoppingmall.board.domain.match.Match;
import com.example.shoppingmall.board.domain.type.MatchCategory;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateMatchRequest {

    @JsonProperty("category")
    private MatchCategory category;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;

    @JsonProperty("locationArea")
    private String locationArea;

    @JsonProperty("locationDetail")
    private String locationDetail;

    @JsonProperty("matchDate")
    private LocalDateTime matchDate;

    @JsonProperty("maxPlayers")
    private Integer maxPlayers;

    @JsonProperty("gameFormat")
    private String gameFormat;

    @JsonProperty("openChatUrl")
    private String openChatUrl;

    @Builder
    public UpdateMatchRequest(MatchCategory category, String title, String content,
                              String locationArea, String locationDetail, LocalDateTime matchDate, Integer maxPlayers, String openChatUrl,
                              String gameFormat) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.locationArea = locationArea;
        this.locationDetail = locationDetail;
        this.matchDate = matchDate;
        this.maxPlayers = maxPlayers;
        this.openChatUrl = openChatUrl;
        this.gameFormat = gameFormat;
    }

    public UpdateMatchRequest(MatchCategory category, String title, String content) {
        this.category = category;
        this.title = title;
        this.content = content;
    }

    public Match toMatchingPost(Member member) {
        return Match.createMatchingPost(
                member,
                this.title,
                this.content,
                this.locationArea,
                this.locationDetail,
                this.matchDate,
                this.maxPlayers,
                this.gameFormat,
                this.openChatUrl
        );
    }
}
