package com.example.shoppingmall.board.domain.match;

import com.example.shoppingmall.auth.domain.member.Member;
import com.example.shoppingmall.board.application.dto.CreateMatchRequest;
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

  @Column(columnDefinition = "TEXT", nullable = false)
  private String content;

  @Column(length = 100)
  private String location;

  @Column(name = "match_date")
  private LocalDateTime matchDate;

  @Column(name = "max_players")
  private Integer maxPlayers;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private MatchStatus status = MatchStatus.RECRUITING;

  @Column(nullable = false)
  private Integer views = 0;

  @Builder
  public Match(Long id, Member member, MatchCategory category, String title, String content,
      String location, LocalDateTime matchDate, Integer maxPlayers) {
    super(id);
    this.member = member;
    this.category = category;
    this.title = title;
    this.content = content;
    this.location = location;
    this.matchDate = matchDate;
    this.maxPlayers = maxPlayers;
  }

  // 정적 팩토리 메서드 - 일반 게시글용
  public static Match createGeneralPost(Member member, MatchCategory category, String title,
      String content) {
    return Match.builder()
        .member(member)
        .category(category)
        .title(title)
        .content(content)
        .build();
  }

  // 정적 팩토리 메서드 - 매칭 게시글용
  public static Match createMatchingPost(Member member, String title, String content,
      String location, LocalDateTime matchDate,
      Integer maxPlayers) {
    return Match.builder()
        .member(member)
        .category(MatchCategory.MATCHING)
        .title(title)
        .content(content)
        .location(location)
        .matchDate(matchDate)
        .maxPlayers(maxPlayers)
        .build();
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

  // 게시글 수정 메서드
  public void updatePost(String title, String content) {
    this.title = title;
    this.content = content;
  }

  // 매칭 게시글 수정 메서드
  public void updateMatchingPost(String title, String content, String location,
      LocalDateTime matchDate, Integer maxPlayers) {
    this.title = title;
    this.content = content;
    this.location = location;
    this.matchDate = matchDate;
    this.maxPlayers = maxPlayers;
  }

  // 작성자 확인 메서드
  public boolean isOwnedBy(Member member) {
    return this.member.equals(member);
  }

  public boolean isOwnedBy(Long memberId) {
    return this.member.getId().equals(memberId);
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