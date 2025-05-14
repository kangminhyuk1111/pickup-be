package com.example.shoppingmall.auth.application.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class GoogleOAuthAccessTokenRequestTest {

  @Test
  void 기본_생성자로_객체_생성() {
    final GoogleOAuthAccessTokenRequest request = new GoogleOAuthAccessTokenRequest();

    assertThat(request.getClientId()).isNull();
    assertThat(request.getClientSecret()).isNull();
    assertThat(request.getCode()).isNull();
    assertThat(request.getGrantType()).isEqualTo("authorization_code");
    assertThat(request.getRedirectUri()).isNull();
  }

  @Test
  void 전체_매개변수_생성자로_객체_생성() {
    final String clientId = "test-client-id";
    final String clientSecret = "test-client-secret";
    final String code = "test-code";
    final String grantType = "authorization_code";
    final String redirectUri = "http://test.com/callback";

    final GoogleOAuthAccessTokenRequest request = new GoogleOAuthAccessTokenRequest(
        clientId, clientSecret, code, grantType, redirectUri);

    assertThat(request.getClientId()).isEqualTo(clientId);
    assertThat(request.getClientSecret()).isEqualTo(clientSecret);
    assertThat(request.getCode()).isEqualTo(code);
    assertThat(request.getGrantType()).isEqualTo(grantType);
    assertThat(request.getRedirectUri()).isEqualTo(redirectUri);
  }

  @Test
  void 간단한_생성자로_객체_생성_기본값_설정됨() {
    final String clientId = "test-client-id";
    final String clientSecret = "test-client-secret";
    final String code = "test-code";

    final GoogleOAuthAccessTokenRequest request = new GoogleOAuthAccessTokenRequest(
        clientId, clientSecret, code);

    assertThat(request.getClientId()).isEqualTo(clientId);
    assertThat(request.getClientSecret()).isEqualTo(clientSecret);
    assertThat(request.getCode()).isEqualTo(code);
    assertThat(request.getGrantType()).isEqualTo("authorization_code");
    assertThat(request.getRedirectUri()).isEqualTo("http://localhost:8080/api/auth/oauth/callback/google");
  }

  @Test
  void 간단한_생성자_null_값_처리() {
    final GoogleOAuthAccessTokenRequest request = new GoogleOAuthAccessTokenRequest(
        null, null, null);

    assertThat(request.getClientId()).isNull();
    assertThat(request.getClientSecret()).isNull();
    assertThat(request.getCode()).isNull();
    assertThat(request.getGrantType()).isEqualTo("authorization_code");
    assertThat(request.getRedirectUri()).isEqualTo("http://localhost:8080/api/auth/oauth/callback/google");
  }

  @Test
  void grant_type_기본값_확인() {
    final GoogleOAuthAccessTokenRequest request = new GoogleOAuthAccessTokenRequest();

    assertThat(request.getGrantType()).isEqualTo("authorization_code");
  }

  @Test
  void 빈_문자열로_객체_생성() {
    final GoogleOAuthAccessTokenRequest request = new GoogleOAuthAccessTokenRequest(
        "", "", "");

    assertThat(request.getClientId()).isEmpty();
    assertThat(request.getClientSecret()).isEmpty();
    assertThat(request.getCode()).isEmpty();
    assertThat(request.getGrantType()).isEqualTo("authorization_code");
    assertThat(request.getRedirectUri()).isEqualTo("http://localhost:8080/api/auth/oauth/callback/google");
  }
}