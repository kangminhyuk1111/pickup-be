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
          .build())
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("제목이 null일 때 예외 발생")
    void validateMatchingPost_nullTitle_throwsException() {
      assertThatThrownBy(() -> CreateMatchRequest.builder()
          .category(MatchCategory.FREE)
          .title(null)
          .content("내용")
          .build())
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("제목이 빈 문자열일 때 예외 발생")
    void validateMatchingPost_emptyTitle_throwsException() {
      assertThatThrownBy(() -> CreateMatchRequest.builder()
          .category(MatchCategory.FREE)
          .title("   ")
          .content("내용")
          .build())
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("내용이 null일 때 예외 발생")
    void validateMatchingPost_nullContent_throwsException() {
      assertThatThrownBy(() -> CreateMatchRequest.builder()
          .category(MatchCategory.FREE)
          .title("제목")
          .content(null)
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
          .title("축구 매칭")
          .content("함께 축구하실 분을 찾습니다")
          .location("서울시 강남구 축구장")
          .matchDate(futureDate)
          .maxPlayers(10)
          .build();

      // then
      assertThat(request.getCategory()).isEqualTo(MatchCategory.MATCHING);
      assertThat(request.getTitle()).isEqualTo("축구 매칭");
      assertThat(request.getContent()).isEqualTo("함께 축구하실 분을 찾습니다");
      assertThat(request.getLocation()).isEqualTo("서울시 강남구 축구장");
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
          "농구장",
          futureDate,
          4
      );

      // then
      assertThat(request.getCategory()).isEqualTo(MatchCategory.MATCHING);
      assertThat(request.getTitle()).isEqualTo("농구 매칭");
      assertThat(request.getLocation()).isEqualTo("농구장");
      assertThat(request.getMaxPlayers()).isEqualTo(4);
      assertThat(request.isMatchingPost()).isTrue();
    }

    // 매칭 게시글 검증 테스트들 (기존과 동일)...
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
          "자유 게시글 내용"
      );

      // then
      assertThat(request.getCategory()).isEqualTo(MatchCategory.FREE);
      assertThat(request.getTitle()).isEqualTo("자유 게시글 제목");
      assertThat(request.getContent()).isEqualTo("자유 게시글 내용");
      assertThat(request.getLocation()).isNull();
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
          "중요한 공지사항입니다"
      );

      // then
      assertThat(request.getCategory()).isEqualTo(MatchCategory.NOTICE);
      assertThat(request.getTitle()).isEqualTo("공지사항");
      assertThat(request.getContent()).isEqualTo("중요한 공지사항입니다");
      assertThat(request.isMatchingPost()).isFalse();
    }
  }

  @Nested
  @DisplayName("toMatch 메서드 테스트")
  class ToMatchTest {

    @Test
    @DisplayName("매칭 게시글을 Match 엔터티로 변환 성공")
    void toMatch_matchingPost_success() {
      // given
      CreateMatchRequest request = CreateMatchRequest.createMatchingPost(
          "농구 매칭",
          "농구하실 분 구합니다",
          "농구장",
          futureDate,
          8
      );

      // when
      Match match = request.toMatchingPost(member);

      // then
      assertThat(match).isNotNull();
      assertThat(match.getMember()).isEqualTo(member);
      assertThat(match.getCategory()).isEqualTo(MatchCategory.MATCHING);
      assertThat(match.getTitle()).isEqualTo("농구 매칭");
      assertThat(match.getContent()).isEqualTo("농구하실 분 구합니다");
      assertThat(match.getLocation()).isEqualTo("농구장");
      assertThat(match.getMatchDate()).isEqualTo(futureDate);
      assertThat(match.getMaxPlayers()).isEqualTo(8);
    }

    @Test
    @DisplayName("일반 게시글을 Match 엔터티로 변환 성공")
    void toMatch_generalPost_success() {
      // given
      CreateMatchRequest request = CreateMatchRequest.createFreePost(
          "자유 게시글",
          "자유 게시글 내용"
      );

      // when
      Match match = request.toMatchingPost(member);

      // then
      assertThat(match).isNotNull();
      assertThat(match.getMember()).isEqualTo(member);
      assertThat(match.getCategory()).isEqualTo(MatchCategory.MATCHING);
      assertThat(match.getTitle()).isEqualTo("자유 게시글");
      assertThat(match.getContent()).isEqualTo("자유 게시글 내용");
      assertThat(match.getLocation()).isNull();
      assertThat(match.getMatchDate()).isNull();
      assertThat(match.getMaxPlayers()).isNull();
    }
  }
}