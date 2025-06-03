package com.example.shoppingmall.board.domain.match;

import com.example.shoppingmall.auth.domain.member.Member;
import com.example.shoppingmall.board.application.dto.UpdateMatchRequest;
import com.example.shoppingmall.board.domain.type.MatchCategory;
import com.example.shoppingmall.board.domain.type.MatchStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

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

  @Nested
  @DisplayName("매칭 게시글 생성 테스트")
  class MatchingPostCreationTest {

    @Test
    @DisplayName("매칭 게시글 생성 성공 - 모든 필드 포함")
    void 매칭_게시글_생성_성공_모든_필드() {
      // given
      final String title = "농구 매칭 모집";
      final String content = "농구 함께 하실 분!";
      final String locationArea = "강남구 대청동";
      final String locationDetail = "대진체육관";
      final Integer maxPlayers = 20;
      final String gameFormat = "5vs5 3파전";
      final String openChatUrl = "https://openchaturl.com";

      // when
      final Match match = Match.createMatchingPost(
          member, title, content, locationArea, locationDetail, matchDate, maxPlayers, gameFormat, openChatUrl
      );

      // then
      assertThat(match.getMember()).isEqualTo(member);
      assertThat(match.getCategory()).isEqualTo(MatchCategory.MATCHING);
      assertThat(match.getTitle()).isEqualTo(title);
      assertThat(match.getContent()).isEqualTo(content);
      assertThat(match.getLocationArea()).isEqualTo(locationArea);
      assertThat(match.getLocationDetail()).isEqualTo(locationDetail);
      assertThat(match.getMatchDate()).isEqualTo(matchDate);
      assertThat(match.getMaxPlayers()).isEqualTo(maxPlayers);
      assertThat(match.getStatus()).isEqualTo(MatchStatus.RECRUITING);
      assertThat(match.getViews()).isEqualTo(0);
    }

    @Test
    @DisplayName("매칭 게시글 생성 성공 - 필수 필드만")
    void 매칭_게시글_생성_성공_필수_필드만() {
      // given
      final String title = "간단한 농구 매칭";
      final String content = "농구 하실 분!";
      final String locationArea = "송파구 잠실동";
      final String locationDetail = "천마 풋살파크";
      final Integer maxPlayers = 20;
      final String gameFormat = "5vs5 3파전";
      final String openChatUrl = "https://openchaturl.com";

      // when
      final Match match = Match.createMatchingPost(
          member, title, content, locationArea, locationDetail, matchDate, maxPlayers, gameFormat, openChatUrl
      );

      // then
      assertThat(match.getMember()).isEqualTo(member);
      assertThat(match.getCategory()).isEqualTo(MatchCategory.MATCHING);
      assertThat(match.getTitle()).isEqualTo(title);
      assertThat(match.getContent()).isEqualTo(content);
      assertThat(match.getLocationArea()).isEqualTo(locationArea);
      assertThat(match.getLocationDetail()).isEqualTo(locationDetail);
      assertThat(match.getMatchDate()).isNotNull();
      assertThat(match.getMaxPlayers()).isEqualTo(20);
      assertThat(match.getStatus()).isEqualTo(MatchStatus.RECRUITING);
    }
  }

  @Nested
  @DisplayName("일반 게시글 생성 테스트")
  class GeneralPostCreationTest {

    @Test
    @DisplayName("자유 게시글 생성 성공")
    void 자유_게시글_생성_성공() {
      // given
      final String title = "자유 게시글";
      final String content = "일반 게시글 내용";
      final String openChatUrl = "http://localhost:8080/";

      // when
      final Match match = Match.createGeneralPost(member, MatchCategory.FREE, title, content, openChatUrl);

      // then
      assertThat(match.getMember()).isEqualTo(member);
      assertThat(match.getCategory()).isEqualTo(MatchCategory.FREE);
      assertThat(match.getTitle()).isEqualTo(title);
      assertThat(match.getContent()).isEqualTo(content);
      assertThat(match.getOpenChatUrl()).isEqualTo(openChatUrl);
      assertThat(match.getLocationArea()).isNull();
      assertThat(match.getLocationDetail()).isNull();
      assertThat(match.getMatchDate()).isNull();
      assertThat(match.getMaxPlayers()).isNull();
      assertThat(match.getStatus()).isEqualTo(MatchStatus.RECRUITING);
    }

    @Test
    @DisplayName("공지 게시글 생성 성공")
    void 공지_게시글_생성_성공() {
      // given
      final String title = "공지사항";
      final String content = "중요한 공지사항입니다";
      final String openChatUrl = "http://localhost:8080/notice";

      // when
      final Match match = Match.createGeneralPost(member, MatchCategory.NOTICE, title, content, openChatUrl);

      // then
      assertThat(match.getMember()).isEqualTo(member);
      assertThat(match.getCategory()).isEqualTo(MatchCategory.NOTICE);
      assertThat(match.getTitle()).isEqualTo(title);
      assertThat(match.getContent()).isEqualTo(content);
      assertThat(match.getOpenChatUrl()).isEqualTo(openChatUrl);
    }
  }

  @Nested
  @DisplayName("조회수 관리 테스트")
  class ViewsManagementTest {

    @Test
    @DisplayName("조회수 증가 - 처음 조회")
    void 조회수_증가_처음_조회() {
      // given
      final String gameFormat = "5vs5 3파전";
      final String openChatUrl = "https://openchaturl.com";
      final Match match = Match.createMatchingPost(
          member, "제목", "내용", "강남구 대청동", "대진체육관", matchDate, 10, gameFormat, openChatUrl
      );

      // when
      match.incrementViews();

      // then
      assertThat(match.getViews()).isEqualTo(1);
    }

    @Test
    @DisplayName("조회수 증가 - 여러번 조회")
    void 조회수_증가_여러번_조회() {
      // given
      final String gameFormat = "6vs6 3파전";
      final String openChatUrl = "https://openchaturl.com";
      final Match match = Match.createMatchingPost(
          member, "제목", "내용", "강남구 대청동", "대진체육관", matchDate, 10, gameFormat, openChatUrl
      );

      // when
      match.incrementViews();
      match.incrementViews();
      match.incrementViews();

      // then
      assertThat(match.getViews()).isEqualTo(3);
    }

    @Test
    @DisplayName("조회수가 null인 경우 처리")
    void 조회수가_null인_경우_처리() {
      // given
      final Match match = Match.builder()
          .member(member)
          .category(MatchCategory.MATCHING)
          .title("제목")
          .content("내용")
          .locationArea("강남구 대청동")
          .locationDetail("대진체육관")
          .build();
      // views를 null로 설정 (실제로는 기본값 0이지만 테스트용)

      // when
      match.incrementViews();

      // then
      assertThat(match.getViews()).isEqualTo(1);
    }
  }

  @Nested
  @DisplayName("게시글 타입 및 상태 확인 테스트")
  class PostTypeAndStatusTest {

    @Test
    @DisplayName("매칭 게시글 여부 확인 - true")
    void 매칭_게시글_여부_확인_true() {
      // given
      final String gameFormat = "6vs6 3파전";
      final String openChatUrl = "https://openchaturl.com";
      final Match match = Match.createMatchingPost(
          member, "제목", "내용", "강남구 대청동", "대진체육관", matchDate, 10, gameFormat, openChatUrl
      );

      // when & then
      assertThat(match.isMatchingPost()).isTrue();
    }

    @Test
    @DisplayName("매칭 게시글 여부 확인 - false")
    void 매칭_게시글_여부_확인_false() {
      // given
      final Match match = Match.createGeneralPost(member, MatchCategory.FREE, "제목", "내용", "http://localhost:8080/");

      // when & then
      assertThat(match.isMatchingPost()).isFalse();
    }

    @Test
    @DisplayName("모집 중 상태 확인")
    void 모집_중_상태_확인() {
      // given
      final String gameFormat = "6vs6 3파전";
      final String openChatUrl = "https://openchaturl.com";
      final Match match = Match.createMatchingPost(
          member, "제목", "내용", "강남구 대청동", "대진체육관", matchDate, 10, gameFormat, openChatUrl
      );

      // when & then
      assertThat(match.isRecruiting()).isTrue();
    }

    @Test
    @DisplayName("매칭 완료 처리 성공")
    void 매칭_완료_처리_성공() {
      // given
      final String gameFormat = "6vs6 3파전";
      final String openChatUrl = "https://openchaturl.com";
      final Match match = Match.createMatchingPost(
          member, "제목", "내용", "강남구 대청동", "대진체육관", matchDate, 10, gameFormat, openChatUrl
      );

      // when
      match.completeMatching();

      // then
      assertThat(match.getStatus()).isEqualTo(MatchStatus.COMPLETED);
      assertThat(match.isRecruiting()).isFalse();
    }

    @Test
    @DisplayName("일반 게시글 매칭 완료 - 상태 변경 안됨")
    void 일반_게시글_매칭_완료_상태_변경_안됨() {
      // given
      final Match match = Match.createGeneralPost(member, MatchCategory.FREE, "제목", "내용", "http://localhost:8080/");

      // when
      match.completeMatching();

      // then
      assertThat(match.getStatus()).isEqualTo(MatchStatus.RECRUITING);
    }

    @Test
    @DisplayName("매칭 종료 처리 성공")
    void 매칭_종료_처리_성공() {
      // given
      final String gameFormat = "6vs6 3파전";
      final String openChatUrl = "https://openchaturl.com";
      final Match match = Match.createMatchingPost(
          member, "제목", "내용", "강남구 대청동", "대진체육관", matchDate, 10, gameFormat, openChatUrl
      );

      // when
      match.finishMatch();

      // then
      assertThat(match.getStatus()).isEqualTo(MatchStatus.FINISHED);
    }

    @Test
    @DisplayName("일반 게시글 매칭 종료 - 상태 변경 안됨")
    void 일반_게시글_매칭_종료_상태_변경_안됨() {
      // given
      final Match match = Match.createGeneralPost(member, MatchCategory.FREE, "제목", "내용", "http://localhost:8080/");

      // when
      match.finishMatch();

      // then
      assertThat(match.getStatus()).isEqualTo(MatchStatus.RECRUITING);
    }
  }

  @Nested
  @DisplayName("게시글 수정 테스트")
  class PostUpdateTest {

    @Test
    @DisplayName("일반 게시글 수정 성공")
    void 일반_게시글_수정_성공() {
      // given
      final Match match = Match.createGeneralPost(member, MatchCategory.FREE, "원래 제목", "원래 내용", "http://localhost:8080/");
      final String newTitle = "수정된 제목";
      final String newContent = "수정된 내용";

      // when
      match.updatePost(newTitle, newContent);

      // then
      assertThat(match.getTitle()).isEqualTo(newTitle);
      assertThat(match.getContent()).isEqualTo(newContent);
    }

    @Test
    @DisplayName("매칭 게시글 수정 성공")
    void 매칭_게시글_수정_성공() {
      // given
      final String gameFormat = "6vs6 3파전";
      final String openChatUrl = "https://openchaturl.com";
      final Match match = Match.createMatchingPost(
          member, "원래 제목", "원래 내용", "원래 지역", "원래 장소", matchDate, 10, gameFormat, openChatUrl
      );

      final String newTitle = "수정된 제목";
      final String newContent = "수정된 내용";
      final String newLocationArea = "수정된 지역";
      final String newLocationDetail = "수정된 장소";
      final LocalDateTime newMatchDate = matchDate.plusDays(1);
      final Integer newMaxPlayers = 12;
      final String newOpenChatUrl = "http://localhost:8080/updated";

      final UpdateMatchRequest updateRequest = new UpdateMatchRequest(
          MatchCategory.MATCHING, newTitle, newContent, newLocationArea,
          newLocationDetail, newMatchDate, newMaxPlayers, newOpenChatUrl, gameFormat
      );

      // when
      match.updateMatchingPost(updateRequest);

      // then
      assertThat(match.getTitle()).isEqualTo(newTitle);
      assertThat(match.getContent()).isEqualTo(newContent);
      assertThat(match.getLocationArea()).isEqualTo(newLocationArea);
      assertThat(match.getLocationDetail()).isEqualTo(newLocationDetail);
      assertThat(match.getMatchDate()).isEqualTo(newMatchDate);
      assertThat(match.getMaxPlayers()).isEqualTo(newMaxPlayers);
    }

    @Test
    @DisplayName("매칭 게시글 수정 - 일부 필드만 변경")
    void 매칭_게시글_수정_일부_필드만_변경() {
      // given
      final String gameFormat = "6vs6 3파전";
      final String openChatUrl = "https://openchaturl.com";
      final Match match = Match.createMatchingPost(
          member, "원래 제목", "원래 내용", "원래 지역", "원래 장소", matchDate, 10, gameFormat, openChatUrl
      );

      final String newTitle = "수정된 제목";
      final String newContent = "수정된 내용";
      final UpdateMatchRequest updateRequest = new UpdateMatchRequest(
          MatchCategory.MATCHING, newTitle, newContent, "원래 지역",
          "원래 장소", matchDate, 10, null, gameFormat
      );

      // when
      match.updateMatchingPost(updateRequest);

      // then
      assertThat(match.getTitle()).isEqualTo(newTitle);
      assertThat(match.getContent()).isEqualTo(newContent);
      assertThat(match.getLocationArea()).isEqualTo("원래 지역");
      assertThat(match.getLocationDetail()).isEqualTo("원래 장소");
      assertThat(match.getMatchDate()).isEqualTo(matchDate);
      assertThat(match.getMaxPlayers()).isEqualTo(10);
    }
  }

  @Nested
  @DisplayName("작성자 확인 테스트")
  class OwnershipTest {

    @Test
    @DisplayName("작성자 확인 - Member 객체로 확인 성공")
    void 작성자_확인_Member_객체로_확인_성공() {
      // given
      final String gameFormat = "6vs6 3파전";
      final String openChatUrl = "https://openchaturl.com";
      final Match match = Match.createMatchingPost(
          member, "제목", "내용", "강남구 대청동", "대진체육관", matchDate, 10, gameFormat, openChatUrl
      );

      // when & then
      assertThat(match.isOwnedBy(member)).isTrue();
    }

    @Test
    @DisplayName("작성자 확인 - Member 객체로 확인 실패")
    void 작성자_확인_Member_객체로_확인_실패() {
      // given
      final String gameFormat = "6vs6 3파전";
      final String openChatUrl = "https://openchaturl.com";
      final Match match = Match.createMatchingPost(
          member, "제목", "내용", "강남구 대청동", "대진체육관", matchDate, 10, gameFormat, openChatUrl
      );
      final Member otherMember = Member.builder().id(2L).build();

      System.out.println("Match member ID: " + match.getMember().getId());
      System.out.println("Other member ID: " + otherMember.getId());
      System.out.println("Match member equals otherMember: " + match.getMember().equals(otherMember));

      // when & then
      assertThat(match.isOwnedBy(otherMember)).isFalse();
    }


    @Test
    @DisplayName("작성자 확인 - memberId로 확인 성공")
    void 작성자_확인_memberId로_확인_성공() {
      // given
      final String gameFormat = "6vs6 3파전";
      final String openChatUrl = "https://openchaturl.com";
      final Match match = Match.createMatchingPost(
          member, "제목", "내용", "강남구 대청동", "대진체육관", matchDate, 10, gameFormat, openChatUrl
      );

      // when & then
      assertThat(match.isOwnedBy(1L)).isTrue();
    }

    @Test
    @DisplayName("작성자 확인 - memberId로 확인 실패")
    void 작성자_확인_memberId로_확인_실패() {
      // given
      final String gameFormat = "6vs6 3파전";
      final String openChatUrl = "https://openchaturl.com";
      final Match match = Match.createMatchingPost(
          member, "제목", "내용", "강남구 대청동", "대진체육관", matchDate, 10, gameFormat, openChatUrl
      );

      // when & then
      assertThat(match.isOwnedBy(2L)).isFalse();
    }
  }

  @Nested
  @DisplayName("equals 및 hashCode 테스트")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("equals - 같은 ID를 가진 Match 객체는 동일함")
    void equals_같은_ID를_가진_Match_객체는_동일함() {
      // given
      final Match match1 = Match.builder()
          .id(1L)
          .member(member)
          .category(MatchCategory.MATCHING)
          .title("제목")
          .content("내용")
          .locationArea("지역")
          .locationDetail("장소")
          .build();

      final Match match2 = Match.builder()
          .id(1L)
          .member(member)
          .category(MatchCategory.MATCHING)
          .title("다른 제목")
          .content("다른 내용")
          .locationArea("다른 지역")
          .locationDetail("다른 장소")
          .build();

      // when & then
      assertThat(match1).isEqualTo(match2);
      assertThat(match1.hashCode()).isEqualTo(match2.hashCode());
    }

    @Test
    @DisplayName("equals - 다른 ID를 가진 Match 객체는 다름")
    void equals_다른_ID를_가진_Match_객체는_다름() {
      // given
      final Match match1 = Match.builder()
          .id(1L)
          .member(member)
          .category(MatchCategory.MATCHING)
          .title("제목")
          .content("내용")
          .locationArea("지역")
          .locationDetail("장소")
          .build();

      final Match match2 = Match.builder()
          .id(2L)
          .member(member)
          .category(MatchCategory.MATCHING)
          .title("제목")
          .content("내용")
          .locationArea("지역")
          .locationDetail("장소")
          .build();

      // when & then
      assertThat(match1).isNotEqualTo(match2);
    }

    @Test
    @DisplayName("equals - ID가 null인 경우 동일하지 않음")
    void equals_ID가_null인_경우_동일하지_않음() {
      // given
      final Match match1 = Match.builder()
          .member(member)
          .category(MatchCategory.MATCHING)
          .title("제목")
          .content("내용")
          .locationArea("지역")
          .locationDetail("장소")
          .build();

      final Match match2 = Match.builder()
          .member(member)
          .category(MatchCategory.MATCHING)
          .title("제목")
          .content("내용")
          .locationArea("지역")
          .locationDetail("장소")
          .build();

      // when & then
      assertThat(match1).isNotEqualTo(match2);
    }

    @Test
    @DisplayName("equals - 자기 자신과는 동일함")
    void equals_자기_자신과는_동일함() {
      // given
      final String gameFormat = "6vs6 3파전";
      final String openChatUrl = "https://openchaturl.com";
      final Match match = Match.createMatchingPost(
          member, "제목", "내용", "강남구 대청동", "대진체육관", matchDate, 10, gameFormat, openChatUrl
      );

      // when & then
      assertThat(match).isEqualTo(match);
    }

    @Test
    @DisplayName("equals - null과는 동일하지 않음")
    void equals_null과는_동일하지_않음() {
      // given
      final String gameFormat = "6vs6 3파전";
      final String openChatUrl = "https://openchaturl.com";
      final Match match = Match.createMatchingPost(
          member, "제목", "내용", "강남구 대청동", "대진체육관", matchDate, 10, gameFormat, openChatUrl
      );

      // when & then
      assertThat(match).isNotEqualTo(null);
    }

    @Test
    @DisplayName("equals - 다른 클래스와는 동일하지 않음")
    void equals_다른_클래스와는_동일하지_않음() {
      // given
      final String gameFormat = "6vs6 3파전";
      final String openChatUrl = "https://openchaturl.com";
      final Match match = Match.createMatchingPost(
          member, "제목", "내용", "강남구 대청동", "대진체육관", matchDate, 10, gameFormat, openChatUrl
      );
      final String other = "not a match";

      // when & then
      assertThat(match).isNotEqualTo(other);
    }
  }

  @Nested
  @DisplayName("Edge Cases 테스트")
  class EdgeCasesTest {

    @Test
    @DisplayName("Builder로 최소 필드만 설정하여 생성")
    void Builder로_최소_필드만_설정하여_생성() {
      // given & when
      final Match match = Match.builder()
          .member(member)
          .category(MatchCategory.FREE)
          .title("최소 제목")
          .content("최소 내용")
          .build();

      // then
      assertThat(match.getMember()).isEqualTo(member);
      assertThat(match.getCategory()).isEqualTo(MatchCategory.FREE);
      assertThat(match.getTitle()).isEqualTo("최소 제목");
      assertThat(match.getContent()).isEqualTo("최소 내용");
      assertThat(match.getLocationArea()).isNull();
      assertThat(match.getLocationDetail()).isNull();
      assertThat(match.getMatchDate()).isNull();
      assertThat(match.getMaxPlayers()).isNull();
      assertThat(match.getOpenChatUrl()).isNull();
      assertThat(match.getStatus()).isEqualTo(MatchStatus.RECRUITING);
      assertThat(match.getViews()).isEqualTo(0);
    }

    @Test
    @DisplayName("매칭 게시글에서 maxPlayers가 null")
    void 매칭_게시글에서_maxPlayers가_null() {
      // given & when
      final String gameFormat = "6vs6 3파전";
      final String openChatUrl = "https://openchaturl.com";
      final Match match = Match.createMatchingPost(
          member, "제목", "내용", "강남구 대청동", "대진체육관", matchDate, null, gameFormat, openChatUrl
      );

      // then
      assertThat(match.getMaxPlayers()).isNull();
      assertThat(match.isMatchingPost()).isTrue();
    }

    @Test
    @DisplayName("매칭 게시글에서 matchDate가 null")
    void 매칭_게시글에서_matchDate가_null() {
      // given & when
      final String gameFormat = "6vs6 3파전";
      final String openChatUrl = "https://openchaturl.com";
      final Match match = Match.createMatchingPost(
          member, "제목", "내용", "강남구 대청동", "대진체육관", null, 10, gameFormat, openChatUrl
      );

      // then
      assertThat(match.getMatchDate()).isNull();
      assertThat(match.isMatchingPost()).isTrue();
    }

    @Test
    @DisplayName("긴 content 필드 테스트 (CLOB)")
    void 긴_content_필드_테스트() {
      // given
      final String longContent = "a".repeat(10000); // 10,000자
      final String gameFormat = "6vs6 3파전";
      final String openChatUrl = "https://openchaturl.com";

      // when
      final Match match = Match.createMatchingPost(
          member, "제목", longContent, "강남구 대청동", "대진체육관", matchDate, 10, gameFormat, openChatUrl
      );

      // then
      assertThat(match.getContent()).isEqualTo(longContent);
      assertThat(match.getContent().length()).isEqualTo(10000);
    }
  }
}