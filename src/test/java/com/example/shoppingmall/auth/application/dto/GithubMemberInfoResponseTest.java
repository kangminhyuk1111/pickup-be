package com.example.shoppingmall.auth.application.dto;

import com.example.shoppingmall.auth.domain.type.ProviderType;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class GithubMemberInfoResponseTest {

  @Test
  void 깃허브_정보를_DB에_저장할_OAuthMemberInfoResponse로_변경() {
    final GithubMemberInfoResponse info = new GithubMemberInfoResponse("1", "login",
        "http://localhost:1111");

    final OAuthMemberInfoResponse oAuthMemberInfoResponse = info.toOAuthMemberInfoResponse();

    assertThat(oAuthMemberInfoResponse.getOauthId()).isEqualTo(info.getId());
    assertThat(oAuthMemberInfoResponse.getProfileUrl()).isEqualTo(info.getAvatarUrl());
    assertThat(oAuthMemberInfoResponse.getProviderType()).isEqualTo(ProviderType.GITHUB);
    assertThat(oAuthMemberInfoResponse.getName()).isEqualTo(info.getName());
  }

  @Test
  void id가_null일_때_예외_발생() {
    assertThatThrownBy(() ->
        new GithubMemberInfoResponse(null, "testuser", "http://avatar.url")
            .toOAuthMemberInfoResponse())
        .isInstanceOf(NullPointerException.class)
        .hasMessage("id 는 null이 될 수 없습니다.");
  }

  @Test
  void name이_null일_때_예외_발생() {
    assertThatThrownBy(() ->
        new GithubMemberInfoResponse("123", null, "http://avatar.url")
            .toOAuthMemberInfoResponse())
        .isInstanceOf(NullPointerException.class)
        .hasMessage("name 은 null이 될 수 없습니다.");
  }

  @Test
  void avatarUrl이_null일_때_예외_발생() {
    assertThatThrownBy(() ->
        new GithubMemberInfoResponse("123", "testuser", null)
            .toOAuthMemberInfoResponse())
        .isInstanceOf(NullPointerException.class)
        .hasMessage("avatarUrl 은 null이 될 수 없습니다.");
  }

  @Test
  void 모든_필드가_null일_때_id_검증이_먼저_실행됨() {
    assertThatThrownBy(() ->
        new GithubMemberInfoResponse(null, null, null)
            .toOAuthMemberInfoResponse())
        .isInstanceOf(NullPointerException.class)
        .hasMessage("id 는 null이 될 수 없습니다.");
  }

  @Test
  void 유효한_모든_값으로_변환_성공() {
    final String id = "12345";
    final String name = "octocat";
    final String avatarUrl = "https://avatars.githubusercontent.com/u/583231?v=4";

    final GithubMemberInfoResponse info = new GithubMemberInfoResponse(id, name, avatarUrl);
    final OAuthMemberInfoResponse result = info.toOAuthMemberInfoResponse();

    assertThat(result.getOauthId()).isEqualTo(id);
    assertThat(result.getName()).isEqualTo(name);
    assertThat(result.getProfileUrl()).isEqualTo(avatarUrl);
    assertThat(result.getProviderType()).isEqualTo(ProviderType.GITHUB);
  }

  @Test
  void 빈_문자열_값으로_변환_성공() {
    final GithubMemberInfoResponse info = new GithubMemberInfoResponse("", "", "");
    final OAuthMemberInfoResponse result = info.toOAuthMemberInfoResponse();

    assertThat(result.getOauthId()).isEmpty();
    assertThat(result.getName()).isEmpty();
    assertThat(result.getProfileUrl()).isEmpty();
    assertThat(result.getProviderType()).isEqualTo(ProviderType.GITHUB);
  }

  @Test
  void 특수문자_포함_값으로_변환_성공() {
    final String id = "user-123";
    final String name = "test@user.com";
    final String avatarUrl = "https://example.com/avatar?size=200&format=jpg";

    final GithubMemberInfoResponse info = new GithubMemberInfoResponse(id, name, avatarUrl);
    final OAuthMemberInfoResponse result = info.toOAuthMemberInfoResponse();

    assertThat(result.getOauthId()).isEqualTo(id);
    assertThat(result.getName()).isEqualTo(name);
    assertThat(result.getProfileUrl()).isEqualTo(avatarUrl);
    assertThat(result.getProviderType()).isEqualTo(ProviderType.GITHUB);
  }

  @Test
  void validateFields_메서드_단독_테스트() {
    final GithubMemberInfoResponse validInfo = new GithubMemberInfoResponse("123", "user", "url");

    // 예외가 발생하지 않음을 확인
    assertThatCode(() -> validInfo.toOAuthMemberInfoResponse())
        .doesNotThrowAnyException();
  }
}