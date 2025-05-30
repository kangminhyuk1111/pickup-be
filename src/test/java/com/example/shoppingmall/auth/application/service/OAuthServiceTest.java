package com.example.shoppingmall.auth.application.service;

import com.example.shoppingmall.auth.application.client.OAuthClient;
import com.example.shoppingmall.auth.application.dto.OAuthAccessTokenResponse;
import com.example.shoppingmall.auth.application.dto.OAuthMemberInfoResponse;
import com.example.shoppingmall.auth.application.dto.SigninResponse;
import com.example.shoppingmall.auth.application.token.TokenProvider;
import com.example.shoppingmall.auth.domain.member.Member;
import com.example.shoppingmall.auth.domain.member.MemberRepository;
import com.example.shoppingmall.auth.domain.type.ProviderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OAuthServiceTest {

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private OAuthClient githubOAuthClient;

  @Mock
  private OAuthClient googleOAuthClient;

  @Mock
  private OAuthClient kakaoOauthClient;

  @Mock
  private TokenProvider tokenProvider;

  private OAuthService oAuthService;

  @BeforeEach
  void setUp() {
    oAuthService = new OAuthService(memberRepository, githubOAuthClient, googleOAuthClient, kakaoOauthClient, tokenProvider);
  }

  @Test
  @DisplayName("GitHub 로그인 성공 - 기존 회원")
  void github_signin_success_existing_member() {
    // given
    String code = "test-code";
    String provider = "github";
    String accessToken = "access-token";
    String oauthId = "github-12345";

    OAuthAccessTokenResponse tokenResponse = new OAuthAccessTokenResponse(accessToken);
    OAuthMemberInfoResponse memberInfoResponse = new OAuthMemberInfoResponse(
        oauthId, "testuser", "https://github.com/avatar.jpg", ProviderType.GITHUB);

    Member existingMember = Member.builder()
        .id(1L)
        .oauthId(oauthId)
        .name("testuser")
        .profileUrl("https://github.com/avatar.jpg")
        .providerType(ProviderType.GITHUB)
        .build();

    when(githubOAuthClient.getAccessToken(code)).thenReturn(tokenResponse);
    when(githubOAuthClient.getMemberInfo(accessToken)).thenReturn(memberInfoResponse);
    when(memberRepository.findByOauthId(oauthId)).thenReturn(Optional.of(existingMember));
    when(tokenProvider.createToken("1")).thenReturn("jwt-token");

    // when
    SigninResponse response = oAuthService.signin(code, provider);

    // then
    assertThat(response.getToken()).isEqualTo("jwt-token");
    verify(memberRepository, never()).save(any(Member.class)); // 새로 저장하지 않음
    verify(tokenProvider).createToken("1");
  }

  @Test
  @DisplayName("GitHub 로그인 성공 - 신규 회원")
  void github_signin_success_new_member() {
    // given
    String code = "test-code";
    String provider = "github";
    String accessToken = "access-token";
    String oauthId = "github-67890";

    OAuthAccessTokenResponse tokenResponse = new OAuthAccessTokenResponse(accessToken);
    OAuthMemberInfoResponse memberInfoResponse = new OAuthMemberInfoResponse(
        oauthId, "newuser", "https://github.com/newavatar.jpg", ProviderType.GITHUB);

    Member newMember = Member.builder()
        .id(2L)
        .oauthId(oauthId)
        .name("newuser")
        .profileUrl("https://github.com/newavatar.jpg")
        .providerType(ProviderType.GITHUB)
        .build();

    when(githubOAuthClient.getAccessToken(code)).thenReturn(tokenResponse);
    when(githubOAuthClient.getMemberInfo(accessToken)).thenReturn(memberInfoResponse);
    when(memberRepository.findByOauthId(oauthId)).thenReturn(Optional.empty());
    when(memberRepository.save(any(Member.class))).thenReturn(newMember);
    when(tokenProvider.createToken("2")).thenReturn("jwt-token-new");

    // when
    SigninResponse response = oAuthService.signin(code, provider);

    // then
    assertThat(response.getToken()).isEqualTo("jwt-token-new");
    verify(memberRepository).save(any(Member.class)); // 새로 저장됨
    verify(tokenProvider).createToken("2");
  }

  @Test
  @DisplayName("Google 로그인 성공")
  void google_signin_success() {
    // given
    String code = "test-code";
    String provider = "google";
    String accessToken = "access-token";
    String oauthId = "google-12345";

    OAuthAccessTokenResponse tokenResponse = new OAuthAccessTokenResponse(accessToken);
    OAuthMemberInfoResponse memberInfoResponse = new OAuthMemberInfoResponse(
        oauthId, "googleuser", "https://google.com/avatar.jpg", ProviderType.GOOGLE);

    Member member = Member.builder()
        .id(3L)
        .oauthId(oauthId)
        .name("googleuser")
        .profileUrl("https://google.com/avatar.jpg")
        .providerType(ProviderType.GOOGLE)
        .build();

    when(googleOAuthClient.getAccessToken(code)).thenReturn(tokenResponse);
    when(googleOAuthClient.getMemberInfo(accessToken)).thenReturn(memberInfoResponse);
    when(memberRepository.findByOauthId(oauthId)).thenReturn(Optional.of(member));
    when(tokenProvider.createToken("3")).thenReturn("jwt-token-google");

    // when
    SigninResponse response = oAuthService.signin(code, provider);

    // then
    assertThat(response.getToken()).isEqualTo("jwt-token-google");
    verify(googleOAuthClient).getAccessToken(code);
    verify(googleOAuthClient).getMemberInfo(accessToken);
  }

  @Test
  @DisplayName("지원하지 않는 OAuth 제공자로 로그인 시 예외 발생")
  void signin_with_unsupported_provider_throws_exception() {
    // given
    String code = "test-code";
    String unsupportedProvider = "kakao";

    // when & then
    assertThatThrownBy(() -> oAuthService.signin(code, unsupportedProvider))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("지원하지 않는 OAuth 제공자: " + unsupportedProvider);
  }

  @Test
  @DisplayName("대소문자 구분 없이 provider 처리")
  void signin_provider_case_insensitive() {
    // given
    String code = "test-code";
    String accessToken = "access-token";
    String oauthId = "github-case-test";

    OAuthAccessTokenResponse tokenResponse = new OAuthAccessTokenResponse(accessToken);
    OAuthMemberInfoResponse memberInfoResponse = new OAuthMemberInfoResponse(
        oauthId, "caseuser", "https://github.com/case.jpg", ProviderType.GITHUB);

    Member member = Member.builder()
        .id(4L)
        .oauthId(oauthId)
        .name("caseuser")
        .profileUrl("https://github.com/case.jpg")
        .providerType(ProviderType.GITHUB)
        .build();

    when(githubOAuthClient.getAccessToken(code)).thenReturn(tokenResponse);
    when(githubOAuthClient.getMemberInfo(accessToken)).thenReturn(memberInfoResponse);
    when(memberRepository.findByOauthId(oauthId)).thenReturn(Optional.of(member));
    when(tokenProvider.createToken("4")).thenReturn("jwt-token-case");

    // when
    SigninResponse response1 = oAuthService.signin(code, "GITHUB");
    SigninResponse response2 = oAuthService.signin(code, "GitHub");
    SigninResponse response3 = oAuthService.signin(code, "github");

    // then
    assertThat(response1.getToken()).isEqualTo("jwt-token-case");
    assertThat(response2.getToken()).isEqualTo("jwt-token-case");
    assertThat(response3.getToken()).isEqualTo("jwt-token-case");
    verify(githubOAuthClient, times(3)).getAccessToken(code);
  }

  @Test
  @DisplayName("OAuth 클라이언트에서 예외 발생 시 런타임 예외 발생")
  void signin_when_oauth_client_throws_exception() {
    // given
    String code = "invalid-code";
    String provider = "github";

    when(githubOAuthClient.getAccessToken(code))
        .thenThrow(new HttpClientErrorException(org.springframework.http.HttpStatus.BAD_REQUEST));

    // when & then
    assertThatThrownBy(() -> oAuthService.signin(code, provider))
        .isInstanceOf(RuntimeException.class)
        .hasMessage(code + "를 이용해서 OAuth 정보를 받아오는 데 실패했습니다.");
  }

  @Test
  @DisplayName("회원 정보 조회 중 예외 발생")
  void signin_when_get_member_info_throws_exception() {
    // given
    String code = "test-code";
    String provider = "github";
    String accessToken = "invalid-token";

    OAuthAccessTokenResponse tokenResponse = new OAuthAccessTokenResponse(accessToken);

    when(githubOAuthClient.getAccessToken(code)).thenReturn(tokenResponse);
    when(githubOAuthClient.getMemberInfo(accessToken))
        .thenThrow(new HttpClientErrorException(org.springframework.http.HttpStatus.UNAUTHORIZED));

    // when & then
    assertThatThrownBy(() -> oAuthService.signin(code, provider))
        .isInstanceOf(RuntimeException.class)
        .hasMessage(code + "를 이용해서 OAuth 정보를 받아오는 데 실패했습니다.");
  }

  @Test
  @DisplayName("토큰 생성 실패 시 예외 전파")
  void signin_when_token_creation_fails() {
    // given
    String code = "test-code";
    String provider = "github";
    String accessToken = "access-token";
    String oauthId = "github-token-fail";

    OAuthAccessTokenResponse tokenResponse = new OAuthAccessTokenResponse(accessToken);
    OAuthMemberInfoResponse memberInfoResponse = new OAuthMemberInfoResponse(
        oauthId, "tokenuser", "https://github.com/token.jpg", ProviderType.GITHUB);

    Member member = Member.builder()
        .id(5L)
        .oauthId(oauthId)
        .name("tokenuser")
        .profileUrl("https://github.com/token.jpg")
        .providerType(ProviderType.GITHUB)
        .build();

    when(githubOAuthClient.getAccessToken(code)).thenReturn(tokenResponse);
    when(githubOAuthClient.getMemberInfo(accessToken)).thenReturn(memberInfoResponse);
    when(memberRepository.findByOauthId(oauthId)).thenReturn(Optional.of(member));
    when(tokenProvider.createToken("5")).thenThrow(new RuntimeException("토큰 생성 실패"));

    // when & then
    assertThatThrownBy(() -> oAuthService.signin(code, provider))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("토큰 생성 실패");
  }

  @Test
  @DisplayName("회원 저장 실패 시 예외 전파")
  void signin_when_member_save_fails() {
    // given
    String code = "test-code";
    String provider = "github";
    String accessToken = "access-token";
    String oauthId = "github-save-fail";

    OAuthAccessTokenResponse tokenResponse = new OAuthAccessTokenResponse(accessToken);
    OAuthMemberInfoResponse memberInfoResponse = new OAuthMemberInfoResponse(
        oauthId, "saveuser", "https://github.com/save.jpg", ProviderType.GITHUB);

    when(githubOAuthClient.getAccessToken(code)).thenReturn(tokenResponse);
    when(githubOAuthClient.getMemberInfo(accessToken)).thenReturn(memberInfoResponse);
    when(memberRepository.findByOauthId(oauthId)).thenReturn(Optional.empty());
    when(memberRepository.save(any(Member.class))).thenThrow(new RuntimeException("회원 저장 실패"));

    // when & then
    assertThatThrownBy(() -> oAuthService.signin(code, provider))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("회원 저장 실패");
  }

  @Test
  @DisplayName("전체 플로우 검증 - 메서드 호출 순서")
  void signin_full_flow_verification() {
    // given
    String code = "test-code";
    String provider = "github";
    String accessToken = "access-token";
    String oauthId = "github-flow-test";

    OAuthAccessTokenResponse tokenResponse = new OAuthAccessTokenResponse(accessToken);
    OAuthMemberInfoResponse memberInfoResponse = new OAuthMemberInfoResponse(
        oauthId, "flowuser", "https://github.com/flow.jpg", ProviderType.GITHUB);

    Member member = Member.builder()
        .id(6L)
        .oauthId(oauthId)
        .name("flowuser")
        .profileUrl("https://github.com/flow.jpg")
        .providerType(ProviderType.GITHUB)
        .build();

    when(githubOAuthClient.getAccessToken(code)).thenReturn(tokenResponse);
    when(githubOAuthClient.getMemberInfo(accessToken)).thenReturn(memberInfoResponse);
    when(memberRepository.findByOauthId(oauthId)).thenReturn(Optional.of(member));
    when(tokenProvider.createToken("6")).thenReturn("jwt-token-flow");

    // when
    oAuthService.signin(code, provider);

    // then - 메서드 호출 순서 검증
    var inOrder = inOrder(githubOAuthClient, memberRepository, tokenProvider);
    inOrder.verify(githubOAuthClient).getAccessToken(code);
    inOrder.verify(githubOAuthClient).getMemberInfo(accessToken);
    inOrder.verify(memberRepository).findByOauthId(oauthId);
    inOrder.verify(tokenProvider).createToken("6");
  }
}