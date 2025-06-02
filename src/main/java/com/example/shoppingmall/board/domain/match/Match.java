package com.example.shoppingmall.board.domain.match;

import com.example.shoppingmall.auth.domain.member.Member;
import com.example.shoppingmall.board.application.dto.CreateMatchRequest;
import com.example.shoppingmall.board.application.dto.UpdateMatchRequest;
import com.example.shoppingmall.board.domain.type.MatchCategory;
import com.example.shoppingmall.board.domain.type.MatchStatus;
import com.example.shoppingmall.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Getter
@NoArgsConstructor
public class Match extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private Member member;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private MatchCategory category;

  @Column(nullable = false, length = 200)
  private String title;

  @Lob
  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(name = "location_area", length = 100)
  private String locationArea;

  @Column(name = "location_detail", length = 200)
  private String locationDetail;

  @Column(name = "match_date")
  private LocalDateTime matchDate;

  @Column(name = "max_players")
  private Integer maxPlayers;

  @Column(name = "current_players")
  private Integer currentPlayers;

  @Column(name = "game_format")
  private String gameFormat;

  @Column(name = "open_chat_url")
  private String openChatUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private MatchStatus status = MatchStatus.RECRUITING;

  @Column(nullable = false)
  private Integer views = 0;

  @Builder
  public Match(Long id, Member member, MatchCategory category, String title, String content,
      String locationArea, String locationDetail, LocalDateTime matchDate, Integer maxPlayers,
      Integer currentPlayers, String gameFormat, String openChatUrl) {
    super(id);
    this.member = member;
    this.category = category;
    this.title = title;
    this.content = content;
    this.locationArea = locationArea;
    this.locationDetail = locationDetail;
    this.matchDate = matchDate;
    this.maxPlayers = maxPlayers;
    this.currentPlayers = currentPlayers != null ? currentPlayers : 0;
    this.gameFormat = gameFormat;
    this.openChatUrl = openChatUrl;
  }

  // 정적 팩토리 메서드 - 일반 게시글용 (자유, 공지)
  public static Match createGeneralPost(Member member, MatchCategory category, String title,
      String content, String openChatUrl) {
    return Match.builder()
        .member(member)
        .category(category)
        .title(title)
        .content(content)
        .openChatUrl(openChatUrl)
        .currentPlayers(0)
        .build();
  }

  // 정적 팩토리 메서드 - 매칭 게시글용
  public static Match createMatchingPost(Member member, String title, String content,
      String locationArea, String locationDetail, LocalDateTime matchDate,
      Integer maxPlayers, String gameFormat) {
    return Match.builder()
        .member(member)
        .category(MatchCategory.MATCHING)
        .title(title)
        .content(content)
        .locationArea(locationArea)
        .locationDetail(locationDetail)
        .matchDate(matchDate)
        .maxPlayers(maxPlayers)
        .currentPlayers(0)
        .gameFormat(gameFormat)
        .build();
  }

  // CreateMatchRequest를 받는 팩토리 메서드
  public static Match createFromRequest(Member member, CreateMatchRequest request) {
    if (request.getCategory() == MatchCategory.MATCHING) {
      return createMatchingPost(
          member,
          request.getTitle(),
          request.getContent(),
          request.getLocationArea(),
          request.getLocationDetail(),
          request.getMatchDate(),
          request.getMaxPlayers(),
          request.getGameFormat()
      );
    } else {
      return createGeneralPost(
          member,
          request.getCategory(),
          request.getTitle(),
          request.getContent(),
          request.getOpenChatUrl()
      );
    }
  }

  // 비즈니스 로직 메서드
  public void incrementViews() {
    this.views = this.views == null ? 1 : this.views + 1;
  }

  public boolean isMatchingPost() {
    return this.category == MatchCategory.MATCHING;
  }

  public boolean isRecruiting() {
    return this.status == MatchStatus.RECRUITING;
  }

  public boolean isFull() {
    return this.maxPlayers != null && this.currentPlayers != null
        && this.currentPlayers >= this.maxPlayers;
  }

  public boolean canJoin() {
    return isRecruiting() && !isFull();
  }

  public void addPlayer() {
    if (canJoin()) {
      this.currentPlayers = this.currentPlayers == null ? 1 : this.currentPlayers + 1;

      // 정원이 찼으면 자동으로 모집 완료 처리
      if (isFull()) {
        this.status = MatchStatus.COMPLETED;
      }
    } else {
      throw new IllegalStateException("참가할 수 없는 매치입니다.");
    }
  }

  public void removePlayer() {
    if (this.currentPlayers != null && this.currentPlayers > 0) {
      this.currentPlayers = this.currentPlayers - 1;

      // 모집 완료 상태였다면 다시 모집중으로 변경
      if (this.status == MatchStatus.COMPLETED) {
        this.status = MatchStatus.RECRUITING;
      }
    }
  }

  public void completeMatching() {
    if (this.category == MatchCategory.MATCHING) {
      this.status = MatchStatus.COMPLETED;
    }
  }

  public void finishMatch() {
    if (this.category == MatchCategory.MATCHING) {
      this.status = MatchStatus.FINISHED;
    }
  }

  public void reopenMatching() {
    if (this.category == MatchCategory.MATCHING &&
        (this.status == MatchStatus.COMPLETED || this.status == MatchStatus.FINISHED)) {
      this.status = MatchStatus.RECRUITING;
    }
  }

  // 업데이트 메서드들
  public void updatePost(String title, String content) {
    this.title = title;
    this.content = content;
  }

  public void updateGeneralPost(String title, String content, String openChatUrl) {
    this.title = title;
    this.content = content;
    this.openChatUrl = openChatUrl;
  }

  public void updateMatchingPost(UpdateMatchRequest request) {
    this.title = request.getTitle();
    this.content = request.getContent();
    this.locationArea = request.getLocationArea();
    this.locationDetail = request.getLocationDetail();
    this.matchDate = request.getMatchDate();
    this.maxPlayers = request.getMaxPlayers();
    this.gameFormat = request.getGameFormat();
    this.openChatUrl = request.getOpenChatUrl();

    // 최대 인원이 변경되었을 때 상태 재계산
    if (isFull() && this.status == MatchStatus.RECRUITING) {
      this.status = MatchStatus.COMPLETED;
    } else if (!isFull() && this.status == MatchStatus.COMPLETED) {
      this.status = MatchStatus.RECRUITING;
    }
  }

  // 권한 체크 메서드들
  public boolean isOwnedBy(Member member) {
    return this.member.equals(member);
  }

  public boolean isOwnedBy(Long memberId) {
    return this.member.getId().equals(memberId);
  }

  // 정보 제공 메서드들
  public int getRemainingSlots() {
    if (this.maxPlayers == null || this.currentPlayers == null) {
      return 0;
    }
    return Math.max(0, this.maxPlayers - this.currentPlayers);
  }

  public double getParticipationRate() {
    if (this.maxPlayers == null || this.maxPlayers == 0 || this.currentPlayers == null) {
      return 0.0;
    }
    return (double) this.currentPlayers / this.maxPlayers * 100;
  }

  public boolean isMatchDatePassed() {
    return this.matchDate != null && this.matchDate.isBefore(LocalDateTime.now());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Match match = (Match) o;
    return getId() != null && getId().equals(match.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}