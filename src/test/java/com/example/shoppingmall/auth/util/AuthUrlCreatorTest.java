package com.example.shoppingmall.auth.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class AuthUrlCreatorTest {

  private AuthUrlCreator authUrlCreator;

  @BeforeEach
  void setUp() {
    authUrlCreator = new AuthUrlCreator();
  }

  @Test
  @DisplayName("GitHub OAuth URL을 올바르게 생성한다")
  void githubAuthUrl_creates_correct_url() {
    // given
    String clientId = "test-github-client-id";

    // when
    String result = authUrlCreator.githubAuthUrl(clientId);

    // then
    assertThat(result).startsWith("redirect:");

    String actualUrl = result.substring("redirect:".length());
    assertThat(actualUrl).startsWith("https://github.com/login/oauth/authorize");
    assertThat(actualUrl).contains("client_id=" + clientId);
    assertThat(actualUrl).contains("redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fapi%2Fauth%2Foauth%2Fcallback%2Fgithub");
    assertThat(actualUrl).contains("scope=user%3Aemail");
  }

  @Test
  @DisplayName("Google OAuth URL을 올바르게 생성한다")
  void googleAuthUrl_creates_correct_url() {
    // given
    String clientId = "test-google-client-id";

    // when
    String result = authUrlCreator.googleAuthUrl(clientId);

    // then
    assertThat(result).startsWith("redirect:");

    String actualUrl = result.substring("redirect:".length());
    assertThat(actualUrl).startsWith("https://accounts.google.com/o/oauth2/v2/auth");
    assertThat(actualUrl).contains("client_id=" + clientId);
    assertThat(actualUrl).contains("redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fapi%2Fauth%2Foauth%2Fcallback%2Fgoogle");
    assertThat(actualUrl).contains("scope=profile+email+openid"); // + 형태로 인코딩됨
    assertThat(actualUrl).contains("response_type=code");
    assertThat(actualUrl).contains("access_type=offline");
  }

  @Test
  @DisplayName("GitHub URL의 모든 파라미터가 URL 인코딩되어 있다")
  void githubAuthUrl_parameters_are_url_encoded() {
    // given
    String clientId = "test client!@#$%";

    // when
    String result = authUrlCreator.githubAuthUrl(clientId);

    // then
    String actualUrl = result.substring("redirect:".length());

    // URL 디코딩하여 파라미터 추출
    String[] params = actualUrl.split("\\?")[1].split("&");

    boolean clientIdFound = false;
    boolean redirectUriFound = false;
    boolean scopeFound = false;

    for (String param : params) {
      String[] keyValue = param.split("=", 2);
      String key = keyValue[0];
      String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8) : "";

      switch (key) {
        case "client_id":
          assertThat(value).isEqualTo(clientId);
          clientIdFound = true;
          break;
        case "redirect_uri":
          assertThat(value).isEqualTo("http://localhost:8080/api/auth/oauth/callback/github");
          redirectUriFound = true;
          break;
        case "scope":
          assertThat(value).isEqualTo("user:email");
          scopeFound = true;
          break;
      }
    }

    assertThat(clientIdFound).isTrue();
    assertThat(redirectUriFound).isTrue();
    assertThat(scopeFound).isTrue();
  }

  @Test
  @DisplayName("Google URL의 모든 파라미터가 URL 인코딩되어 있다")
  void googleAuthUrl_parameters_are_url_encoded() {
    // given
    String clientId = "test client!@#$%";

    // when
    String result = authUrlCreator.googleAuthUrl(clientId);

    // then
    String actualUrl = result.substring("redirect:".length());

    // URL 디코딩하여 파라미터 추출
    String[] params = actualUrl.split("\\?")[1].split("&");

    boolean clientIdFound = false;
    boolean redirectUriFound = false;
    boolean scopeFound = false;
    boolean responseTypeFound = false;
    boolean accessTypeFound = false;

    for (String param : params) {
      String[] keyValue = param.split("=", 2);
      String key = keyValue[0];
      String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8) : "";

      switch (key) {
        case "client_id":
          assertThat(value).isEqualTo(clientId);
          clientIdFound = true;
          break;
        case "redirect_uri":
          assertThat(value).isEqualTo("http://localhost:8080/api/auth/oauth/callback/google");
          redirectUriFound = true;
          break;
        case "scope":
          assertThat(value).isEqualTo("profile email openid");
          scopeFound = true;
          break;
        case "response_type":
          assertThat(value).isEqualTo("code");
          responseTypeFound = true;
          break;
        case "access_type":
          assertThat(value).isEqualTo("offline");
          accessTypeFound = true;
          break;
      }
    }

    assertThat(clientIdFound).isTrue();
    assertThat(redirectUriFound).isTrue();
    assertThat(scopeFound).isTrue();
    assertThat(responseTypeFound).isTrue();
    assertThat(accessTypeFound).isTrue();
  }

  @Test
  @DisplayName("특수문자가 포함된 클라이언트 ID도 올바르게 처리한다")
  void handles_special_characters_in_client_id() {
    // given
    String specialClientId = "123-abc_456+789@example.com";

    // when
    String githubUrl = authUrlCreator.githubAuthUrl(specialClientId);
    String googleUrl = authUrlCreator.googleAuthUrl(specialClientId);

    // then
    assertThat(githubUrl).contains("client_id=");
    assertThat(googleUrl).contains("client_id=");

    // URL에서 클라이언트 ID 부분 추출하여 검증
    String githubActualUrl = githubUrl.substring("redirect:".length());
    String googleActualUrl = googleUrl.substring("redirect:".length());

    // 인코딩된 클라이언트 ID가 포함되어 있는지 확인
    assertThat(githubActualUrl).contains("client_id=123-abc_456%2B789%40example.com");
    assertThat(googleActualUrl).contains("client_id=123-abc_456%2B789%40example.com");
  }

  @Test
  @DisplayName("빈 클라이언트 ID도 처리할 수 있다")
  void handles_empty_client_id() {
    // given
    String emptyClientId = "";

    // when
    String githubUrl = authUrlCreator.githubAuthUrl(emptyClientId);
    String googleUrl = authUrlCreator.googleAuthUrl(emptyClientId);

    // then
    assertThat(githubUrl).contains("client_id=");
    assertThat(googleUrl).contains("client_id=");
    assertThat(githubUrl).startsWith("redirect:");
    assertThat(googleUrl).startsWith("redirect:");
  }

  @Test
  @DisplayName("GitHub과 Google URL이 서로 다른 redirect_uri를 가진다")
  void github_and_google_have_different_redirect_uris() {
    // given
    String clientId = "test-client-id";

    // when
    String githubUrl = authUrlCreator.githubAuthUrl(clientId);
    String googleUrl = authUrlCreator.googleAuthUrl(clientId);

    // then
    assertThat(githubUrl).contains("%2Fcallback%2Fgithub"); // URL 인코딩된 형태로 확인
    assertThat(googleUrl).contains("%2Fcallback%2Fgoogle"); // URL 인코딩된 형태로 확인
    assertThat(githubUrl).doesNotContain("%2Fcallback%2Fgoogle");
    assertThat(googleUrl).doesNotContain("%2Fcallback%2Fgithub");
  }

  @Test
  @DisplayName("GitHub과 Google이 서로 다른 OAuth 엔드포인트를 사용한다")
  void github_and_google_use_different_oauth_endpoints() {
    // given
    String clientId = "test-client-id";

    // when
    String githubUrl = authUrlCreator.githubAuthUrl(clientId);
    String googleUrl = authUrlCreator.googleAuthUrl(clientId);

    // then
    String githubActualUrl = githubUrl.substring("redirect:".length());
    String googleActualUrl = googleUrl.substring("redirect:".length());

    assertThat(githubActualUrl).startsWith("https://github.com/login/oauth/authorize");
    assertThat(googleActualUrl).startsWith("https://accounts.google.com/o/oauth2/v2/auth");
  }

  @Test
  @DisplayName("GitHub과 Google이 서로 다른 scope를 요청한다")
  void github_and_google_request_different_scopes() {
    // given
    String clientId = "test-client-id";

    // when
    String githubUrl = authUrlCreator.githubAuthUrl(clientId);
    String googleUrl = authUrlCreator.googleAuthUrl(clientId);

    // then
    assertThat(githubUrl).contains("scope=user%3Aemail");  // user:email
    assertThat(googleUrl).contains("scope=profile+email+openid");  // profile email openid (공백이 +로 인코딩)
  }

  @Test
  @DisplayName("한글이 포함된 클라이언트 ID도 올바르게 인코딩한다")
  void handles_korean_characters_in_client_id() {
    // given
    String koreanClientId = "테스트-클라이언트-아이디";

    // when
    String githubUrl = authUrlCreator.githubAuthUrl(koreanClientId);
    String googleUrl = authUrlCreator.googleAuthUrl(koreanClientId);

    // then
    assertThat(githubUrl).contains("client_id=");
    assertThat(googleUrl).contains("client_id=");

    // 한글이 URL 인코딩되었는지 확인
    String githubActualUrl = githubUrl.substring("redirect:".length());
    String googleActualUrl = googleUrl.substring("redirect:".length());

    assertThat(githubActualUrl).contains("%ED%85%8C%EC%8A%A4%ED%8A%B8"); // '테스트'의 일부
    assertThat(googleActualUrl).contains("%ED%85%8C%EC%8A%A4%ED%8A%B8"); // '테스트'의 일부
  }
}