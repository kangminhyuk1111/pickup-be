package com.example.shoppingmall.board.domain.match;

import com.example.shoppingmall.auth.domain.member.Member;
import com.example.shoppingmall.board.application.dto.UpdateMatchRequest;
import com.example.shoppingmall.board.domain.type.MatchCategory;
import com.example.shoppingmall.board.domain.type.MatchStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class MatchTest {

  private Member member;
  private LocalDateTime matchDate;

  @BeforeEach
  void setUp() {
    member = Member.builder()
        .id(1L)
        .build();
    matchDate = LocalDateTime.now().plusDays(1);
  }

  @Test
  void 매칭_게시글_생성_성공() {
    final String title = "농구 매칭 모집";
    final String content = "농구 함께 하실 분!";
    final String location = "월드컵공원";
    final Integer maxPlayers = 10;

    final Match match = Match.createMatchingPost(member, title, content, location, matchDate, maxPlayers);

    assertThat(match.getMember()).isEqualTo(member);
    assertThat(match.getCategory()).isEqualTo(MatchCategory.MATCHING);
    assertThat(match.getTitle()).isEqualTo(title);
    assertThat(match.getContent()).isEqualTo(content);
    assertThat(match.getLocation()).isEqualTo(location);
    assertThat(match.getMatchDate()).isEqualTo(matchDate);
    assertThat(match.getMaxPlayers()).isEqualTo(maxPlayers);
    assertThat(match.getStatus()).isEqualTo(MatchStatus.RECRUITING);
    assertThat(match.getViews()).isEqualTo(0);
  }

  @Test
  void 일반_게시글_생성_성공() {
    final String title = "자유 게시글";
    final String content = "일반 게시글 내용";
    final String openChatUrl = "http://localhost:8080/";

    final Match match = Match.createGeneralPost(member, MatchCategory.FREE, title, content, openChatUrl);

    assertThat(match.getMember()).isEqualTo(member);
    assertThat(match.getCategory()).isEqualTo(MatchCategory.FREE);
    assertThat(match.getTitle()).isEqualTo(title);
    assertThat(match.getContent()).isEqualTo(content);
    assertThat(match.getLocation()).isNull();
    assertThat(match.getMatchDate()).isNull();
    assertThat(match.getMaxPlayers()).isNull();
    assertThat(match.getStatus()).isEqualTo(MatchStatus.RECRUITING);
  }

  @Test
  void 조회수_증가_처음_조회() {
    final Match match = Match.createMatchingPost(member, "제목", "내용", "장소", matchDate, 10);

    match.incrementViews();

    assertThat(match.getViews()).isEqualTo(1);
  }

  @Test
  void 조회수_증가_여러번_조회() {
    final Match match = Match.createMatchingPost(member, "제목", "내용", "장소", matchDate, 10);

    match.incrementViews();
    match.incrementViews();
    match.incrementViews();

    assertThat(match.getViews()).isEqualTo(3);
  }

  @Test
  void 매칭_게시글_여부_확인_true() {
    final Match match = Match.createMatchingPost(member, "제목", "내용", "장소", matchDate, 10);

    assertThat(match.isMatchingPost()).isTrue();
  }

  @Test
  void 매칭_게시글_여부_확인_false() {
    final Match match = Match.createGeneralPost(member, MatchCategory.FREE, "제목", "내용",  "http://localhost:8080/");

    assertThat(match.isMatchingPost()).isFalse();
  }

  @Test
  void 모집_중_상태_확인() {
    final Match match = Match.createMatchingPost(member, "제목", "내용", "장소", matchDate, 10);

    assertThat(match.isRecruiting()).isTrue();
  }

  @Test
  void 매칭_완료_처리_성공() {
    final Match match = Match.createMatchingPost(member, "제목", "내용", "장소", matchDate, 10);

    match.completeMatching();

    assertThat(match.getStatus()).isEqualTo(MatchStatus.COMPLETED);
    assertThat(match.isRecruiting()).isFalse();
  }

  @Test
  void 일반_게시글_매칭_완료_상태_변경_안됨() {
    final Match match = Match.createGeneralPost(member, MatchCategory.FREE, "제목", "내용",  "http://localhost:8080/");

    match.completeMatching();

    assertThat(match.getStatus()).isEqualTo(MatchStatus.RECRUITING);
  }

  @Test
  void 매칭_종료_처리_성공() {
    final Match match = Match.createMatchingPost(member, "제목", "내용", "장소", matchDate, 10);

    match.finishMatch();

    assertThat(match.getStatus()).isEqualTo(MatchStatus.FINISHED);
  }

  @Test
  void 일반_게시글_매칭_종료_상태_변경_안됨() {
    final Match match = Match.createGeneralPost(member, MatchCategory.FREE, "제목", "내용", "http://localhost:8080/");

    match.finishMatch();

    assertThat(match.getStatus()).isEqualTo(MatchStatus.RECRUITING);
  }

  @Test
  void 일반_게시글_수정_성공() {
    final Match match = Match.createGeneralPost(member, MatchCategory.FREE, "원래 제목", "원래 내용", "http://localhost:8080/");
    final String newTitle = "수정된 제목";
    final String newContent = "수정된 내용";

    match.updatePost(newTitle, newContent);

    assertThat(match.getTitle()).isEqualTo(newTitle);
    assertThat(match.getContent()).isEqualTo(newContent);
  }

  @Test
  void 매칭_게시글_수정_성공() {
    final Match match = Match.createMatchingPost(member, "원래 제목", "원래 내용", "원래 장소", matchDate, 10);
    final String newTitle = "수정된 제목";
    final String newContent = "수정된 내용";
    final String newLocation = "수정된 장소";
    final LocalDateTime newMatchDate = matchDate.plusDays(1);
    final Integer newMaxPlayers = 12;
    final String newOpenChatUrl = "http://localhost:8080/";

    final UpdateMatchRequest updateRequest = new UpdateMatchRequest(MatchCategory.MATCHING, newTitle, newContent, newLocation, newMatchDate, newMaxPlayers, newOpenChatUrl);

    match.updateMatchingPost(updateRequest);

    assertThat(match.getTitle()).isEqualTo(newTitle);
    assertThat(match.getContent()).isEqualTo(newContent);
    assertThat(match.getLocation()).isEqualTo(newLocation);
    assertThat(match.getMatchDate()).isEqualTo(newMatchDate);
    assertThat(match.getMaxPlayers()).isEqualTo(newMaxPlayers);
  }

  @Test
  void 작성자_확인_Member_객체로_확인_성공() {
    final Match match = Match.createMatchingPost(member, "제목", "내용", "장소", matchDate, 10);

    assertThat(match.isOwnedBy(member)).isTrue();
  }

  @Test
  void 작성자_확인_memberId로_확인_성공() {
    final Match match = Match.createMatchingPost(member, "제목", "내용", "장소", matchDate, 10);

    assertThat(match.isOwnedBy(1L)).isTrue();
  }

  @Test
  void 작성자_확인_memberId로_확인_실패() {
    final Match match = Match.createMatchingPost(member, "제목", "내용", "장소", matchDate, 10);

    assertThat(match.isOwnedBy(2L)).isFalse();
  }

  @Test
  void equals_같은_ID를_가진_Match_객체는_동일함() {
    final Match match1 = Match.builder()
        .id(1L)
        .member(member)
        .category(MatchCategory.MATCHING)
        .title("제목")
        .content("내용")
        .build();

    final Match match2 = Match.builder()
        .id(1L)
        .member(member)
        .category(MatchCategory.MATCHING)
        .title("다른 제목")
        .content("다른 내용")
        .build();

    assertThat(match1).isEqualTo(match2);
    assertThat(match1.hashCode()).isEqualTo(match2.hashCode());
  }

  @Test
  void equals_다른_ID를_가진_Match_객체는_다름() {
    final Match match1 = Match.builder()
        .id(1L)
        .member(member)
        .category(MatchCategory.MATCHING)
        .title("제목")
        .content("내용")
        .build();

    final Match match2 = Match.builder()
        .id(2L)
        .member(member)
        .category(MatchCategory.MATCHING)
        .title("제목")
        .content("내용")
        .build();

    assertThat(match1).isNotEqualTo(match2);
  }

  @Test
  void equals_ID가_null인_경우_동일하지_않음() {
    final Match match1 = Match.builder()
        .member(member)
        .category(MatchCategory.MATCHING)
        .title("제목")
        .content("내용")
        .build();

    final Match match2 = Match.builder()
        .member(member)
        .category(MatchCategory.MATCHING)
        .title("제목")
        .content("내용")
        .build();

    assertThat(match1).isNotEqualTo(match2);
  }

  @Test
  void equals_자기_자신과는_동일함() {
    final Match match = Match.createMatchingPost(member, "제목", "내용", "장소", matchDate, 10);

    assertThat(match).isEqualTo(match);
  }

  @Test
  void equals_null과는_동일하지_않음() {
    final Match match = Match.createMatchingPost(member, "제목", "내용", "장소", matchDate, 10);

    assertThat(match).isNotEqualTo(null);
  }

  @Test
  void equals_다른_클래스와는_동일하지_않음() {
    final Match match = Match.createMatchingPost(member, "제목", "내용", "장소", matchDate, 10);
    final String other = "not a match";

    assertThat(match).isNotEqualTo(other);
  }
}