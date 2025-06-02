package com.example.shoppingmall.board.application.dto;

import static org.assertj.core.api.Assertions.*;

import com.example.shoppingmall.auth.domain.member.Member;
import com.example.shoppingmall.board.domain.match.Match;
import com.example.shoppingmall.board.domain.type.MatchCategory;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

class CreateMatchRequestTest {

  private Member member;
  private LocalDateTime futureDate;

  @BeforeEach
  void setUp() {
    member = Member.builder()
        .id(1L)
        .build();

    futureDate = LocalDateTime.now().plusDays(1);
  }

  @Nested
  @DisplayName("기본 필드 검증 테스트")
  class BasicFieldValidationTest {

    @Test
    @DisplayName("카테고리 null일 때 예외 발생")
    void validateMatchingPost_nullCategory_throwsException() {
      assertThatThrownBy(() -> CreateMatchRequest.builder()
          .category(null)
          .title("제목")
          .content("내용")
          .locationArea("강남구 대청동")
          .locationDetail("대진체육관")
          .build())
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("제목이 null일 때 예외 발생")
    void validateMatchingPost_nullTitle_throwsException() {
      assertThatThrownBy(() -> CreateMatchRequest.builder()
          .category(MatchCategory.MATCHING)
          .title(null)
          .content("내용")
          .locationArea("강남구 대청동")
          .locationDetail("대진체육관")
          .build())
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("제목이 빈 문자열일 때 예외 발생")
    void validateMatchingPost_emptyTitle_throwsException() {
      assertThatThrownBy(() -> CreateMatchRequest.builder()
          .category(MatchCategory.MATCHING)
          .title("   ")
          .content("내용")
          .locationArea("강남구 대청동")
          .locationDetail("대진체육관")
          .build())
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("내용이 null일 때 예외 발생")
    void validateMatchingPost_nullContent_throwsException() {
      assertThatThrownBy(() -> CreateMatchRequest.builder()
          .category(MatchCategory.MATCHING)
          .title("제목")
          .content(null)
          .locationArea("강남구 대청동")
          .locationDetail("대진체육관")
          .build())
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("locationArea가 null일 때 예외 발생")
    void validateMatchingPost_nullLocationArea_throwsException() {
      assertThatThrownBy(() -> CreateMatchRequest.builder()
          .category(MatchCategory.MATCHING)
          .title("제목")
          .content("내용")
          .locationArea(null)
          .locationDetail("대진체육관")
          .build())
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("locationDetail이 null일 때 예외 발생")
    void validateMatchingPost_nullLocationDetail_throwsException() {
      assertThatThrownBy(() -> CreateMatchRequest.builder()
          .category(MatchCategory.MATCHING)
          .title("제목")
          .content("내용")
          .locationArea("강남구 대청동")
          .locationDetail(null)
          .build())
          .isInstanceOf(NullPointerException.class);
    }
  }

  @Nested
  @DisplayName("매칭 게시글 생성 테스트")
  class MatchingPostTest {

    @Test
    @DisplayName("유효한 매칭 게시글 생성 성공")
    void createMatchingPost_success() {
      // given & when
      CreateMatchRequest request = CreateMatchRequest.builder()
          .category(MatchCategory.MATCHING)
          .title("농구 매칭")
          .content("함께 농구하실 분을 찾습니다")
          .locationArea("강남구 대청동")
          .locationDetail("대진체육관")
          .matchDate(futureDate)
          .maxPlayers(10)
          .build();

      // then
      assertThat(request.getCategory()).isEqualTo(MatchCategory.MATCHING);
      assertThat(request.getTitle()).isEqualTo("농구 매칭");
      assertThat(request.getContent()).isEqualTo("함께 농구하실 분을 찾습니다");
      assertThat(request.getLocationArea()).isEqualTo("강남구 대청동");
      assertThat(request.getLocationDetail()).isEqualTo("대진체육관");
      assertThat(request.getMatchDate()).isEqualTo(futureDate);
      assertThat(request.getMaxPlayers()).isEqualTo(10);
      assertThat(request.isMatchingPost()).isTrue();
    }

    @Test
    @DisplayName("편의 메서드로 매칭 게시글 생성 성공")
    void createMatchingPost_withStaticMethod_success() {
      // given & when
      CreateMatchRequest request = CreateMatchRequest.createMatchingPost(
          "농구 매칭",
          "농구하실 분 구합니다",
          "송파구 잠실동",
          "천마 풋살파크",
          futureDate,
          8,
          "http://localhost"
      );

      // then
      assertThat(request.getCategory()).isEqualTo(MatchCategory.MATCHING);
      assertThat(request.getTitle()).isEqualTo("농구 매칭");
      assertThat(request.getContent()).isEqualTo("농구하실 분 구합니다");
      assertThat(request.getLocationArea()).isEqualTo("송파구 잠실동");
      assertThat(request.getLocationDetail()).isEqualTo("천마 풋살파크");
      assertThat(request.getMatchDate()).isEqualTo(futureDate);
      assertThat(request.getMaxPlayers()).isEqualTo(8);
      assertThat(request.isMatchingPost()).isTrue();
    }

    @Test
    @DisplayName("매칭 게시글에서 maxPlayers가 null이어도 생성 성공")
    void createMatchingPost_nullMaxPlayers_success() {
      // given & when
      CreateMatchRequest request = CreateMatchRequest.builder()
          .category(MatchCategory.MATCHING)
          .title("농구 매칭")
          .content("농구하실 분 구합니다")
          .locationArea("영등포구 여의도동")
          .locationDetail("EA SPORTS FC")
          .matchDate(futureDate)
          .maxPlayers(null)
          .build();

      // then
      assertThat(request.getMaxPlayers()).isNull();
      assertThat(request.isMatchingPost()).isTrue();
    }

    @Test
    @DisplayName("매칭 게시글에서 matchDate가 null이어도 생성 성공")
    void createMatchingPost_nullMatchDate_success() {
      // given & when
      CreateMatchRequest request = CreateMatchRequest.builder()
          .category(MatchCategory.MATCHING)
          .title("농구 매칭")
          .content("농구하실 분 구합니다")
          .locationArea("강남구 대청동")
          .locationDetail("대진체육관")
          .matchDate(null)
          .maxPlayers(10)
          .build();

      // then
      assertThat(request.getMatchDate()).isNull();
      assertThat(request.isMatchingPost()).isTrue();
    }
  }

  @Nested
  @DisplayName("일반 게시글 생성 테스트")
  class GeneralPostTest {

    @Test
    @DisplayName("자유 게시글 생성 성공")
    void createFreePost_success() {
      // given & when
      CreateMatchRequest request = CreateMatchRequest.createFreePost(
          "자유 게시글 제목",
          "자유 게시글 내용",
          "http://localhost"
      );

      // then
      assertThat(request.getCategory()).isEqualTo(MatchCategory.FREE);
      assertThat(request.getTitle()).isEqualTo("자유 게시글 제목");
      assertThat(request.getContent()).isEqualTo("자유 게시글 내용");
      assertThat(request.getLocationArea()).isNull();
      assertThat(request.getLocationDetail()).isNull();
      assertThat(request.getMatchDate()).isNull();
      assertThat(request.getMaxPlayers()).isNull();
      assertThat(request.isMatchingPost()).isFalse();
    }

    @Test
    @DisplayName("공지 게시글 생성 성공")
    void createNotice_success() {
      // given & when
      CreateMatchRequest request = CreateMatchRequest.createNotice(
          "공지사항",
          "중요한 공지사항입니다",
          "http://localhost"
      );

      // then
      assertThat(request.getCategory()).isEqualTo(MatchCategory.NOTICE);
      assertThat(request.getTitle()).isEqualTo("공지사항");
      assertThat(request.getContent()).isEqualTo("중요한 공지사항입니다");
      assertThat(request.getLocationArea()).isNull();
      assertThat(request.getLocationDetail()).isNull();
      assertThat(request.getMatchDate()).isNull();
      assertThat(request.getMaxPlayers()).isNull();
      assertThat(request.isMatchingPost()).isFalse();
    }
  }

  @Nested
  @DisplayName("toMatchingPost 메서드 테스트")
  class ToMatchingPostTest {

    @Test
    @DisplayName("매칭 게시글을 Match 엔터티로 변환 성공")
    void toMatchingPost_matchingPost_success() {
      // given
      CreateMatchRequest request = CreateMatchRequest.createMatchingPost(
          "농구 매칭",
          "농구하실 분 구합니다",
          "도봉구 쌍문동",
          "루타 풋살장",
          futureDate,
          12,
          "http://localhost"
      );

      // when
      Match match = request.toMatchingPost(member);

      // then
      assertThat(match).isNotNull();
      assertThat(match.getMember()).isEqualTo(member);
      assertThat(match.getCategory()).isEqualTo(MatchCategory.MATCHING);
      assertThat(match.getTitle()).isEqualTo("농구 매칭");
      assertThat(match.getContent()).isEqualTo("농구하실 분 구합니다");
      assertThat(match.getLocationArea()).isEqualTo("도봉구 쌍문동");
      assertThat(match.getLocationDetail()).isEqualTo("루타 풋살장");
      assertThat(match.getMatchDate()).isEqualTo(futureDate);
      assertThat(match.getMaxPlayers()).isEqualTo(12);
    }

    @Test
    @DisplayName("일반 게시글을 Match 엔터티로 변환 - 매칭 타입으로 변환됨")
    void toMatchingPost_generalPost_convertsToMatching() {
      // given
      CreateMatchRequest request = CreateMatchRequest.createFreePost(
          "자유 게시글",
          "자유 게시글 내용",
          "http://localhost"
      );

      // when & then
      assertThatThrownBy(() -> request.toMatchingPost(member)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("필수 필드만으로 매칭 게시글 변환 성공")
    void toMatchingPost_minimalFields_success() {
      // given
      CreateMatchRequest request = CreateMatchRequest.builder()
          .category(MatchCategory.MATCHING)
          .title("간단한 농구 매칭")
          .content("간단한 설명")
          .locationArea("강서구 신정동")
          .locationDetail("신정FC 풋살장")
          .matchDate(null)
          .maxPlayers(null)
          .build();

      // when
      Match match = request.toMatchingPost(member);

      // then
      assertThat(match).isNotNull();
      assertThat(match.getMember()).isEqualTo(member);
      assertThat(match.getCategory()).isEqualTo(MatchCategory.MATCHING);
      assertThat(match.getTitle()).isEqualTo("간단한 농구 매칭");
      assertThat(match.getContent()).isEqualTo("간단한 설명");
      assertThat(match.getLocationArea()).isEqualTo("강서구 신정동");
      assertThat(match.getLocationDetail()).isEqualTo("신정FC 풋살장");
      assertThat(match.getMatchDate()).isNull();
      assertThat(match.getMaxPlayers()).isNull();
    }
  }

  @Nested
  @DisplayName("JSON 매핑 테스트")
  class JsonMappingTest {

    @Test
    @DisplayName("JsonProperty 어노테이션이 올바르게 적용되는지 확인")
    void jsonPropertyMapping_success() {
      // given & when
      CreateMatchRequest request = CreateMatchRequest.builder()
          .category(MatchCategory.MATCHING)
          .title("JSON 테스트")
          .content("JSON 매핑 테스트")
          .locationArea("강북구 수유동")
          .locationDetail("야쿠 풋살 스타디움")
          .matchDate(futureDate)
          .maxPlayers(16)
          .build();

      // then
      assertThat(request.getCategory()).isEqualTo(MatchCategory.MATCHING);
      assertThat(request.getTitle()).isEqualTo("JSON 테스트");
      assertThat(request.getContent()).isEqualTo("JSON 매핑 테스트");
      assertThat(request.getLocationArea()).isEqualTo("강북구 수유동");
      assertThat(request.getLocationDetail()).isEqualTo("야쿠 풋살 스타디움");
      assertThat(request.getMatchDate()).isEqualTo(futureDate);
      assertThat(request.getMaxPlayers()).isEqualTo(16);
    }
  }

  @Nested
  @DisplayName("Edge Cases 테스트")
  class EdgeCasesTest {

    @Test
    @DisplayName("최대 플레이어 수가 0일 때")
    void createMatchingPost_zeroMaxPlayers_success() {
      // given & when
      CreateMatchRequest request = CreateMatchRequest.builder()
          .category(MatchCategory.MATCHING)
          .title("테스트 매칭")
          .content("테스트 내용")
          .locationArea("테스트구 테스트동")
          .locationDetail("테스트 체육관")
          .matchDate(futureDate)
          .maxPlayers(0)
          .build();

      // then
      assertThat(request.getMaxPlayers()).isEqualTo(0);
    }

    @Test
    @DisplayName("과거 날짜로 매치 생성 - 비즈니스 로직에서 검증할 예정")
    void createMatchingPost_pastDate_success() {
      // given
      LocalDateTime pastDate = LocalDateTime.now().minusDays(1);

      // when
      CreateMatchRequest request = CreateMatchRequest.builder()
          .category(MatchCategory.MATCHING)
          .title("과거 매칭")
          .content("과거 날짜 테스트")
          .locationArea("테스트구 테스트동")
          .locationDetail("테스트 체육관")
          .matchDate(pastDate)
          .maxPlayers(10)
          .build();

      // then
      assertThat(request.getMatchDate()).isEqualTo(pastDate);
    }

    @Test
    @DisplayName("매우 긴 제목과 내용으로 생성 성공")
    void createMatchingPost_longContent_success() {
      // given
      String longTitle = "a".repeat(200);
      String longContent = "b".repeat(10000);

      // when
      CreateMatchRequest request = CreateMatchRequest.builder()
          .category(MatchCategory.MATCHING)
          .title(longTitle)
          .content(longContent)
          .locationArea("테스트구 테스트동")
          .locationDetail("테스트 체육관")
          .matchDate(futureDate)
          .maxPlayers(10)
          .build();

      // then
      assertThat(request.getTitle()).isEqualTo(longTitle);
      assertThat(request.getContent()).isEqualTo(longContent);
    }
  }
}