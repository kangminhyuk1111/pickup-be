package com.example.shoppingmall.auth.application.dto;

import com.example.shoppingmall.auth.domain.member.Member;
import com.example.shoppingmall.auth.domain.type.ProviderType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OAuthMemberInfoResponseTest {

  @Test
  void 기본_생성자로_객체_생성() {
    final OAuthMemberInfoResponse response = new OAuthMemberInfoResponse();

    assertThat(response.getOauthId()).isNull();
    assertThat(response.getName()).isNull();
    assertThat(response.getProfileUrl()).isNull();
    assertThat(response.getProviderType()).isNull();
  }

  @Test
  void 전체_매개변수_생성자로_객체_생성() {
    final String oauthId = "12345";
    final String name = "testuser";
    final String profileUrl = "http://example.com/avatar.jpg";
    final ProviderType providerType = ProviderType.GITHUB;

    final OAuthMemberInfoResponse response = new OAuthMemberInfoResponse(
        oauthId, name, profileUrl, providerType);

    assertThat(response.getOauthId()).isEqualTo(oauthId);
    assertThat(response.getName()).isEqualTo(name);
    assertThat(response.getProfileUrl()).isEqualTo(profileUrl);
    assertThat(response.getProviderType()).isEqualTo(providerType);
  }

  @Test
  void GitHub_정보로_Member_엔티티_변환() {
    final OAuthMemberInfoResponse response = new OAuthMemberInfoResponse(
        "github123", "octocat", "https://avatars.github.com/octocat", ProviderType.GITHUB);

    final Member member = response.toMember();

    assertThat(member.getOauthId()).isEqualTo(response.getOauthId());
    assertThat(member.getName()).isEqualTo(response.getName());
    assertThat(member.getProfileUrl()).isEqualTo(response.getProfileUrl());
    assertThat(member.getProviderType()).isEqualTo(response.getProviderType());
  }

  @Test
  void Google_정보로_Member_엔티티_변환() {
    final OAuthMemberInfoResponse response = new OAuthMemberInfoResponse(
        "google456", "testuser", "https://lh3.googleusercontent.com/test", ProviderType.GOOGLE);

    final Member member = response.toMember();

    assertThat(member.getOauthId()).isEqualTo(response.getOauthId());
    assertThat(member.getName()).isEqualTo(response.getName());
    assertThat(member.getProfileUrl()).isEqualTo(response.getProfileUrl());
    assertThat(member.getProviderType()).isEqualTo(ProviderType.GOOGLE);
  }

  @Test
  void null_값들로_Member_엔티티_변환() {
    final OAuthMemberInfoResponse response = new OAuthMemberInfoResponse(
        null, null, null, null);

    final Member member = response.toMember();

    assertThat(member.getOauthId()).isNull();
    assertThat(member.getName()).isNull();
    assertThat(member.getProfileUrl()).isNull();
    assertThat(member.getProviderType()).isNull();
  }

  @Test
  void 빈_문자열로_Member_엔티티_변환() {
    final OAuthMemberInfoResponse response = new OAuthMemberInfoResponse(
        "", "", "", ProviderType.GITHUB);

    final Member member = response.toMember();

    assertThat(member.getOauthId()).isEmpty();
    assertThat(member.getName()).isEmpty();
    assertThat(member.getProfileUrl()).isEmpty();
    assertThat(member.getProviderType()).isEqualTo(ProviderType.GITHUB);
  }

  @Test
  void 특수문자_포함_값으로_Member_엔티티_변환() {
    final String oauthId = "user-123@provider";
    final String name = "테스트 사용자";
    final String profileUrl = "https://example.com/avatar?id=123&size=200";

    final OAuthMemberInfoResponse response = new OAuthMemberInfoResponse(
        oauthId, name, profileUrl, ProviderType.GOOGLE);

    final Member member = response.toMember();

    assertThat(member.getOauthId()).isEqualTo(oauthId);
    assertThat(member.getName()).isEqualTo(name);
    assertThat(member.getProfileUrl()).isEqualTo(profileUrl);
    assertThat(member.getProviderType()).isEqualTo(ProviderType.GOOGLE);
  }

  @Test
  void 모든_ProviderType으로_변환_테스트() {
    for (ProviderType providerType : ProviderType.values()) {
      final OAuthMemberInfoResponse response = new OAuthMemberInfoResponse(
          "oauth" + providerType.name(),
          "user" + providerType.name(),
          "http://profile.url",
          providerType);

      final Member member = response.toMember();

      assertThat(member.getProviderType()).isEqualTo(providerType);
    }
  }

  @Test
  void 긴_문자열_값으로_Member_엔티티_변환() {
    final String longOauthId = "a".repeat(100);
    final String longName = "매우긴사용자이름".repeat(10);
    final String longUrl = "https://verylongdomainname.com/" + "path/".repeat(20) + "avatar.jpg";

    final OAuthMemberInfoResponse response = new OAuthMemberInfoResponse(
        longOauthId, longName, longUrl, ProviderType.GITHUB);

    final Member member = response.toMember();

    assertThat(member.getOauthId()).isEqualTo(longOauthId);
    assertThat(member.getName()).isEqualTo(longName);
    assertThat(member.getProfileUrl()).isEqualTo(longUrl);
    assertThat(member.getProviderType()).isEqualTo(ProviderType.GITHUB);
  }

  @Test
  void toMember_호출시_새로운_Member_객체_생성됨() {
    final OAuthMemberInfoResponse response = new OAuthMemberInfoResponse(
        "123", "user", "url", ProviderType.GITHUB);

    final Member member1 = response.toMember();
    final Member member2 = response.toMember();

    // 다른 인스턴스이지만 같은 값
    assertThat(member1).isNotSameAs(member2);
    assertThat(member1.getOauthId()).isEqualTo(member2.getOauthId());
    assertThat(member1.getName()).isEqualTo(member2.getName());
    assertThat(member1.getProfileUrl()).isEqualTo(member2.getProfileUrl());
    assertThat(member1.getProviderType()).isEqualTo(member2.getProviderType());
  }
}