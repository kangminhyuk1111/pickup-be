package com.example.shoppingmall.board.application.dto;

import com.example.shoppingmall.auth.domain.member.Member;
import com.example.shoppingmall.board.domain.match.Match;
import com.example.shoppingmall.board.domain.type.MatchCategory;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CreateMatchRequest {

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
    public CreateMatchRequest(MatchCategory category, String title, String content,
                              String locationArea, String locationDetail, LocalDateTime matchDate,
                              Integer maxPlayers, String openChatUrl, String gameFormat) {
        validateInput(category, title, content, locationArea, locationDetail);

        this.category = category;
        this.title = title;
        this.content = content;
        this.locationArea = locationArea;
        this.locationDetail = locationDetail;
        this.matchDate = matchDate;
        this.maxPlayers = maxPlayers;
        this.gameFormat = gameFormat;
        this.openChatUrl = openChatUrl;
    }

    public CreateMatchRequest(MatchCategory category, String title, String content,
                              String openChatUrl) {
        validateBasicInput(category, title, content);

        this.category = category;
        this.title = title;
        this.content = content;
        this.openChatUrl = openChatUrl;
    }

    private void validateInput(MatchCategory category, String title, String content,
                               String locationArea, String locationDetail) {
        validateBasicInput(category, title, content);

        if (category == MatchCategory.MATCHING) {
            Objects.requireNonNull(locationArea, "매칭 글의 장소 정보는 필수입니다.");
            Objects.requireNonNull(locationDetail, "매칭 글의 장소 상세는 필수입니다.");

            if (locationArea.trim().isEmpty()) {
                throw new IllegalArgumentException("장소 정보는 빈 값이 될 수 없습니다.");
            }
            if (locationDetail.trim().isEmpty()) {
                throw new IllegalArgumentException("장소 상세는 빈 값이 될 수 없습니다.");
            }
        }
    }

    private void validateBasicInput(MatchCategory category, String title, String content) {
        Objects.requireNonNull(category, "카테고리는 필수입니다.");
        Objects.requireNonNull(title, "제목은 필수입니다.");
        Objects.requireNonNull(content, "내용은 필수입니다.");

        if (title.trim().isEmpty()) {
            throw new IllegalArgumentException("제목은 빈 값이 될 수 없습니다.");
        }
        if (content.trim().isEmpty()) {
            throw new IllegalArgumentException("내용은 빈 값이 될 수 없습니다.");
        }
    }

    public Match toMatchingPost(Member member) {
        if (!isMatchingPost()) {
            throw new RuntimeException("매칭 게시글만 생성할 수 있습니다.");
        }

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

    // 유틸리티 메서드
    public boolean isMatchingPost() {
        return this.category == MatchCategory.MATCHING;
    }

    // 정적 팩토리 메서드들
    public static CreateMatchRequest createFreePost(String title, String content,
                                                    String openChatUrl) {
        return new CreateMatchRequest(MatchCategory.FREE, title, content, openChatUrl);
    }

    public static CreateMatchRequest createNotice(String title, String content, String openChatUrl) {
        return new CreateMatchRequest(MatchCategory.NOTICE, title, content, openChatUrl);
    }

    public static CreateMatchRequest createMatchingPost(String title, String content,
                                                        String locationArea, String locationDetail, LocalDateTime matchDate,
                                                        Integer maxPlayers, String gameFormat) {
        return CreateMatchRequest.builder()
                .category(MatchCategory.MATCHING)
                .title(title)
                .content(content)
                .locationArea(locationArea)
                .locationDetail(locationDetail)
                .matchDate(matchDate)
                .maxPlayers(maxPlayers)
                .gameFormat(gameFormat)
                .build();
    }
}
