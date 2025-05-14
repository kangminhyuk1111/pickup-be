package com.example.shoppingmall.auth.application.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.lang.Long.parseLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenProviderTest {

  private FakeJwtTokenProvider tokenProvider;

  @BeforeEach
  void setUp() {
    tokenProvider = new FakeJwtTokenProvider();
  }

  @Test
  @DisplayName("JWT 토큰 생성에 성공한다.")
  void createToken_success() {
    // given
    String memberId = "id";

    // when
    String token = tokenProvider.createToken(memberId);

    // then
    assertThat(token).isNotBlank();
    assertThat(token).isNotNull();
  }

  @Test
  @DisplayName("JWT 토큰에서 payload를 추출한다.")
  void parsePayload_success() {
    // given
    String memberId = "id";
    String token = tokenProvider.createToken(memberId);

    // when
    String extractedPayload = tokenProvider.parsePayload(token);

    // then
    assertThat(extractedPayload).isEqualTo(memberId.toString());
  }

  @Test
  @DisplayName("서로 다른 멤버 ID로 생성된 토큰은 다른 payload를 가진다.")
  void different_memberIds_produce_different_payloads() {
    // given
    String memberId1 = "id1";
    String memberId2 = "id2";

    // when
    String token1 = tokenProvider.createToken(memberId1);
    String token2 = tokenProvider.createToken(memberId2);
    String payload1 = tokenProvider.parsePayload(token1);
    String payload2 = tokenProvider.parsePayload(token2);

    // then
    assertThat(token1).isNotEqualTo(token2);
    assertThat(payload1).isEqualTo(memberId1);
    assertThat(payload2).isEqualTo(memberId2);
    assertThat(payload1).isNotEqualTo(payload2);
  }

  @Test
  @DisplayName("null 멤버 ID로 토큰 생성시 예외가 발생한다.")
  void createToken_with_null_memberId_throws_exception() {
    // when & then
    assertThatThrownBy(() -> tokenProvider.createToken(null))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("잘못된 토큰에서 payload 추출시 예외가 발생한다.")
  void parsePayload_with_invalid_token_throws_exception() {
    // given
    String invalidToken = "invalid-token";

    // when & then
    assertThatThrownBy(() -> tokenProvider.parsePayload(invalidToken))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("null 토큰에서 payload 추출시 예외가 발생한다.")
  void parsePayload_with_null_token_throws_exception() {
    // when & then
    assertThatThrownBy(() -> tokenProvider.parsePayload(null))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("생성된 토큰이 일관성을 유지한다. (같은 ID로 여러 번 생성해도 동일)")
  void createToken_consistency_check() {
    // given
    String memberId = "id1";

    // when
    String token1 = tokenProvider.createToken(memberId);
    String token2 = tokenProvider.createToken(memberId);

    // then - Fake 구현에서는 같은 ID로 생성하면 같은 토큰이 나와야 함
    assertThat(token1).isEqualTo(token2);

    String payload1 = tokenProvider.parsePayload(token1);
    String payload2 = tokenProvider.parsePayload(token2);
    assertThat(payload1).isEqualTo(payload2);
  }

  @Test
  @DisplayName("토큰 내부의 memberId값을 추출한다.")
  void token_to_userId() {
    // given
    String memberId = "1";

    // when
    String token = tokenProvider.createToken(memberId);
    final Long userId = tokenProvider.getUserId(token);

    // then - Fake 구현에서는 같은 ID로 생성하면 같은 토큰이 나와야 함
    assertThat(userId).isEqualTo(parseLong(memberId));
  }
}