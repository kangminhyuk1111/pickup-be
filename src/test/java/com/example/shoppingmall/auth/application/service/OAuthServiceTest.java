package com.example.shoppingmall.auth.application.service;

import com.example.shoppingmall.auth.application.client.OAuthClient;
import com.example.shoppingmall.auth.application.dto.OAuthAccessTokenResponse;
import com.example.shoppingmall.auth.application.dto.OAuthMemberInfoResponse;
import com.example.shoppingmall.auth.application.dto.SigninResponse;
import com.example.shoppingmall.auth.application.token.TokenProvider;
import com.example.shoppingmall.auth.domain.member.Member;
import com.example.shoppingmall.auth.domain.member.MemberRepository;
import com.example.shoppingmall.auth.domain.type.ProviderType;
import com.example.shoppingmall.core.exception.AuthorizationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
//@DisplayName("OAuth 서비스 테스트")
//class OAuthServiceTest {
//
//  @Mock
//  private MemberRepository memberRepository;
//
//  @Mock
//  private OAuthClient githubOAuthClient;
//
//  @Mock
//  private OAuthClient googleOAuthClient;
//
//  @Mock
//  private OAuthClient kakaoOauthClient;
//
//  @Mock
//  private TokenProvider tokenProvider;
//
//  private OAuthService oAuthService;
//
//  @BeforeEach
//  void setUp() {
//    // 🔥 실제 생성자 방식으로 객체 생성 (@InjectMocks 대신)
//    oAuthService = new OAuthService(
//            memberRepository,
//            githubOAuthClient,
//            googleOAuthClient,
////            kakaoOauthClient,
//            tokenProvider
//    );
//  }
//
//  @Test
//  @DisplayName("GitHub 로그인 성공 - 기존 회원")
//  void github_signin_success_existing_member() {
//    // given
//    String code = "test-code";
//    String provider = "github";
//    String accessToken = "access-token";
//    String oauthId = "github-12345";
//
//    OAuthAccessTokenResponse tokenResponse = new OAuthAccessTokenResponse(accessToken);
//    OAuthMemberInfoResponse memberInfoResponse = new OAuthMemberInfoResponse(
//            oauthId, "testuser", "https://github.com/avatar.jpg", ProviderType.GITHUB);
//
//    Member existingMember = Member.builder()
//            .id(1L)
//            .oauthId(oauthId)
//            .name("testuser")
//            .profileUrl("https://github.com/avatar.jpg")
//            .providerType(ProviderType.GITHUB)
//            .build();
//
//    // 🔥 실제 로직에 맞춘 Mock 설정
//    when(githubOAuthClient.getAccessToken(code)).thenReturn(tokenResponse);
//    when(githubOAuthClient.getMemberInfo(accessToken)).thenReturn(memberInfoResponse);
//    when(memberRepository.existsByOauthId(oauthId)).thenReturn(true); // 🔥 기존 회원
//    when(memberRepository.findByOauthId(oauthId)).thenReturn(Optional.of(existingMember));
//    when(tokenProvider.createToken("1")).thenReturn("jwt-token");
//
//    // when
//    SigninResponse response = oAuthService.signin(code, provider);
//
//    // then
//    assertThat(response.getToken()).isEqualTo("jwt-token");
//    assertThat(response.isNewUser()).isFalse(); // 🔥 기존 회원이므로 false
//    assertThat(response.getName()).isEqualTo("testuser");
//
//    // 검증
//    verify(githubOAuthClient).getAccessToken(code);
//    verify(githubOAuthClient).getMemberInfo(accessToken);
//    verify(memberRepository).existsByOauthId(oauthId);
//    verify(memberRepository).findByOauthId(oauthId);
//    verify(tokenProvider).createToken("1"); // 🔥 기존 회원은 일반 토큰
//    verify(tokenProvider, never()).createTempToken(anyString(), anyString(), any(ProviderType.class));
//    verify(memberRepository, never()).save(any(Member.class));
//  }
//
//  @Test
//  @DisplayName("GitHub 로그인 성공 - 신규 회원")
//  void github_signin_success_new_member() {
//    // given
//    String code = "new-user-code";
//    String provider = "github";
//    String accessToken = "new-access-token";
//    String oauthId = "github-new-user";
//
//    OAuthAccessTokenResponse tokenResponse = new OAuthAccessTokenResponse(accessToken);
//    OAuthMemberInfoResponse memberInfoResponse = new OAuthMemberInfoResponse(
//            oauthId, "newuser", "https://github.com/newavatar.jpg", ProviderType.GITHUB);
//
//    Member savedMember = Member.builder()
//            .id(2L)
//            .oauthId(oauthId)
//            .name("newuser")
//            .profileUrl("https://github.com/newavatar.jpg")
//            .providerType(ProviderType.GITHUB)
//            .build();
//
//    // 🔥 신규 회원 Mock 설정
//    when(githubOAuthClient.getAccessToken(code)).thenReturn(tokenResponse);
//    when(githubOAuthClient.getMemberInfo(accessToken)).thenReturn(memberInfoResponse);
//    when(memberRepository.existsByOauthId(oauthId)).thenReturn(false); // 🔥 신규 회원
//    when(memberRepository.findByOauthId(oauthId)).thenReturn(Optional.empty()); // 🔥 1번만 호출됨
//    when(memberRepository.save(any(Member.class))).thenReturn(savedMember);
//    when(tokenProvider.createTempToken(oauthId, "newuser", ProviderType.GITHUB))
//            .thenReturn("jwt-temp-token");
//
//    // when
//    SigninResponse response = oAuthService.signin(code, provider);
//
//    // then
//    assertThat(response.getToken()).isEqualTo("jwt-temp-token");
//    assertThat(response.isNewUser()).isTrue(); // 🔥 신규 회원이므로 true
//    assertThat(response.getName()).isEqualTo("newuser");
//
//    // 검증
//    verify(githubOAuthClient).getAccessToken(code);
//    verify(githubOAuthClient).getMemberInfo(accessToken);
//    verify(memberRepository).existsByOauthId(oauthId);
//    verify(memberRepository).findByOauthId(oauthId); // 🔥 1번만 호출됨
//    verify(memberRepository).save(any(Member.class));
//    verify(tokenProvider).createTempToken(oauthId, "newuser", ProviderType.GITHUB); // 🔥 신규 회원은 임시 토큰
//    verify(tokenProvider, never()).createToken(anyString());
//  }
//
//  @Test
//  @DisplayName("Google 로그인 성공 - 기존 회원")
//  void google_signin_success_existing_member() {
//    // given
//    String code = "google-code";
//    String provider = "google";
//    String accessToken = "google-access-token";
//    String oauthId = "google-67890";
//
//    OAuthAccessTokenResponse tokenResponse = new OAuthAccessTokenResponse(accessToken);
//    OAuthMemberInfoResponse memberInfoResponse = new OAuthMemberInfoResponse(
//            oauthId, "googleuser", "https://google.com/avatar.jpg", ProviderType.GOOGLE);
//
//    Member existingMember = Member.builder()
//            .id(3L)
//            .oauthId(oauthId)
//            .name("googleuser")
//            .profileUrl("https://google.com/avatar.jpg")
//            .providerType(ProviderType.GOOGLE)
//            .build();
//
//    when(googleOAuthClient.getAccessToken(code)).thenReturn(tokenResponse);
//    when(googleOAuthClient.getMemberInfo(accessToken)).thenReturn(memberInfoResponse);
//    when(memberRepository.existsByOauthId(oauthId)).thenReturn(true);
//    when(memberRepository.findByOauthId(oauthId)).thenReturn(Optional.of(existingMember));
//    when(tokenProvider.createToken("3")).thenReturn("jwt-token-google");
//
//    // when
//    SigninResponse response = oAuthService.signin(code, provider);
//
//    // then
//    assertThat(response.getToken()).isEqualTo("jwt-token-google");
//    assertThat(response.isNewUser()).isFalse();
//    assertThat(response.getName()).isEqualTo("googleuser");
//
//    verify(googleOAuthClient).getAccessToken(code);
//    verify(googleOAuthClient).getMemberInfo(accessToken);
//    verify(memberRepository).existsByOauthId(oauthId);
//    verify(memberRepository).findByOauthId(oauthId);
//    verify(tokenProvider).createToken("3");
//    verify(memberRepository, never()).save(any(Member.class));
//  }
//
//  @Test
//  @DisplayName("Google 로그인 성공 - 신규 회원")
//  void google_signin_success_new_member() {
//    // given
//    String code = "google-new-code";
//    String provider = "google";
//    String accessToken = "google-new-token";
//    String oauthId = "google-new-123";
//
//    OAuthAccessTokenResponse tokenResponse = new OAuthAccessTokenResponse(accessToken);
//    OAuthMemberInfoResponse memberInfoResponse = new OAuthMemberInfoResponse(
//            oauthId, "newgoogleuser", "https://google.com/new.jpg", ProviderType.GOOGLE);
//
//    Member savedMember = Member.builder()
//            .id(4L)
//            .oauthId(oauthId)
//            .name("newgoogleuser")
//            .profileUrl("https://google.com/new.jpg")
//            .providerType(ProviderType.GOOGLE)
//            .build();
//
//    when(googleOAuthClient.getAccessToken(code)).thenReturn(tokenResponse);
//    when(googleOAuthClient.getMemberInfo(accessToken)).thenReturn(memberInfoResponse);
//    when(memberRepository.existsByOauthId(oauthId)).thenReturn(false);
//    when(memberRepository.findByOauthId(oauthId)).thenReturn(Optional.empty()); // 🔥 1번만 호출
//    when(memberRepository.save(any(Member.class))).thenReturn(savedMember);
//    when(tokenProvider.createTempToken(oauthId, "newgoogleuser", ProviderType.GOOGLE))
//            .thenReturn("jwt-temp-google-token");
//
//    // when
//    SigninResponse response = oAuthService.signin(code, provider);
//
//    // then
//    assertThat(response.getToken()).isEqualTo("jwt-temp-google-token");
//    assertThat(response.isNewUser()).isTrue();
//    assertThat(response.getName()).isEqualTo("newgoogleuser");
//
//    verify(googleOAuthClient).getAccessToken(code);
//    verify(googleOAuthClient).getMemberInfo(accessToken);
//    verify(memberRepository).existsByOauthId(oauthId);
//    verify(memberRepository).findByOauthId(oauthId); // 🔥 1번만 호출
//    verify(memberRepository).save(any(Member.class));
//    verify(tokenProvider).createTempToken(oauthId, "newgoogleuser", ProviderType.GOOGLE);
//    verify(tokenProvider, never()).createToken(anyString());
//  }
//
//  @Test
//  @DisplayName("Provider 대소문자 구분 없이 처리")
//  void signin_provider_case_insensitive() {
//    // given
//    String code = "case-code";
//    String accessToken = "case-access-token";
//    String oauthId = "github-case-test";
//
//    OAuthAccessTokenResponse tokenResponse = new OAuthAccessTokenResponse(accessToken);
//    OAuthMemberInfoResponse memberInfoResponse = new OAuthMemberInfoResponse(
//            oauthId, "caseuser", "https://github.com/case.jpg", ProviderType.GITHUB);
//
//    Member existingMember = Member.builder()
//            .id(5L)
//            .oauthId(oauthId)
//            .name("caseuser")
//            .profileUrl("https://github.com/case.jpg")
//            .providerType(ProviderType.GITHUB)
//            .build();
//
//    when(githubOAuthClient.getAccessToken(code)).thenReturn(tokenResponse);
//    when(githubOAuthClient.getMemberInfo(accessToken)).thenReturn(memberInfoResponse);
//    when(memberRepository.existsByOauthId(oauthId)).thenReturn(true);
//    when(memberRepository.findByOauthId(oauthId)).thenReturn(Optional.of(existingMember));
//    when(tokenProvider.createToken("5")).thenReturn("jwt-token-case");
//
//    // when - 여러 케이스 테스트
//    SigninResponse response1 = oAuthService.signin(code, "GITHUB");
//    SigninResponse response2 = oAuthService.signin(code, "GitHub");
//    SigninResponse response3 = oAuthService.signin(code, "github");
//
//    // then
//    assertThat(response1.getToken()).isEqualTo("jwt-token-case");
//    assertThat(response2.getToken()).isEqualTo("jwt-token-case");
//    assertThat(response3.getToken()).isEqualTo("jwt-token-case");
//    verify(githubOAuthClient, times(3)).getAccessToken(code);
//  }
//
//  @Test
//  @DisplayName("지원하지 않는 Provider 예외")
//  void signin_unsupported_provider() {
//    // given
//    String code = "test-code";
//    String provider = "facebook"; // 지원하지 않는 provider
//
//    // when & then
//    assertThatThrownBy(() -> oAuthService.signin(code, provider))
//            .isInstanceOf(AuthorizationException.class);
//  }
//
//  @Test
//  @DisplayName("Access Token이 null인 경우 예외 발생")
//  void signin_null_access_token() {
//    // given
//    String code = "invalid-code";
//    String provider = "github";
//
//    when(githubOAuthClient.getAccessToken(code)).thenReturn(null);
//
//    // when & then
//    assertThatThrownBy(() -> oAuthService.signin(code, provider))
//            .isInstanceOf(AuthorizationException.class);
//
//    verify(githubOAuthClient).getAccessToken(code);
//    verify(githubOAuthClient, never()).getMemberInfo(anyString());
//  }
//
//  @Test
//  @DisplayName("Access Token이 빈 문자열인 경우 예외 발생")
//  void signin_empty_access_token() {
//    // given
//    String code = "empty-token-code";
//    String provider = "github";
//
//    OAuthAccessTokenResponse emptyTokenResponse = new OAuthAccessTokenResponse("");
//    when(githubOAuthClient.getAccessToken(code)).thenReturn(emptyTokenResponse);
//
//    // when & then
//    assertThatThrownBy(() -> oAuthService.signin(code, provider))
//            .isInstanceOf(AuthorizationException.class);
//
//    verify(githubOAuthClient).getAccessToken(code);
//    verify(githubOAuthClient, never()).getMemberInfo(anyString());
//  }
//
//  @Test
//  @DisplayName("OAuth 클라이언트에서 예외 발생")
//  void signin_oauth_client_exception() {
//    // given
//    String code = "exception-code";
//    String provider = "github";
//
//    when(githubOAuthClient.getAccessToken(code))
//            .thenThrow(new RuntimeException("OAuth 연결 실패"));
//
//    // when & then
//    assertThatThrownBy(() -> oAuthService.signin(code, provider))
//            .isInstanceOf(RuntimeException.class)
//            .hasMessage("OAuth 연결 실패");
//
//    verify(githubOAuthClient).getAccessToken(code);
//  }
//
//  @Test
//  @DisplayName("전체 플로우 검증 - 기존 사용자")
//  void signin_full_flow_existing_user() {
//    // given
//    String code = "flow-code";
//    String provider = "github";
//    String accessToken = "flow-token";
//    String oauthId = "github-flow-existing";
//
//    OAuthAccessTokenResponse tokenResponse = new OAuthAccessTokenResponse(accessToken);
//    OAuthMemberInfoResponse memberInfoResponse = new OAuthMemberInfoResponse(
//            oauthId, "flowuser", "https://github.com/flow.jpg", ProviderType.GITHUB);
//
//    Member existingMember = Member.builder()
//            .id(6L)
//            .oauthId(oauthId)
//            .name("flowuser")
//            .profileUrl("https://github.com/flow.jpg")
//            .providerType(ProviderType.GITHUB)
//            .build();
//
//    when(githubOAuthClient.getAccessToken(code)).thenReturn(tokenResponse);
//    when(githubOAuthClient.getMemberInfo(accessToken)).thenReturn(memberInfoResponse);
//    when(memberRepository.existsByOauthId(oauthId)).thenReturn(true);
//    when(memberRepository.findByOauthId(oauthId)).thenReturn(Optional.of(existingMember));
//    when(tokenProvider.createToken("6")).thenReturn("jwt-flow-token");
//
//    // when
//    SigninResponse response = oAuthService.signin(code, provider);
//
//    // then
//    assertThat(response).isNotNull();
//    assertThat(response.getToken()).isEqualTo("jwt-flow-token");
//    assertThat(response.isNewUser()).isFalse();
//    assertThat(response.getName()).isEqualTo("flowuser");
//
//    // 🔥 실제 호출 순서에 맞춘 검증
//    InOrder inOrder = inOrder(githubOAuthClient, memberRepository, tokenProvider);
//    inOrder.verify(githubOAuthClient).getAccessToken(code);
//    inOrder.verify(githubOAuthClient).getMemberInfo(accessToken);
//    inOrder.verify(memberRepository).existsByOauthId(oauthId); // 🔥 existsByOauthId 먼저
//    inOrder.verify(memberRepository).findByOauthId(oauthId);
//    inOrder.verify(tokenProvider).createToken("6");
//
//    verify(memberRepository, never()).save(any(Member.class));
//    verify(tokenProvider, never()).createTempToken(anyString(), anyString(), any(ProviderType.class));
//  }
//
//  @Test
//  @DisplayName("ArgumentCaptor를 사용한 회원 저장 검증")
//  void signin_member_save_verification() {
//    // given
//    String code = "save-test-code";
//    String provider = "github";
//    String accessToken = "save-test-token";
//    String oauthId = "github-save-test";
//
//    OAuthAccessTokenResponse tokenResponse = new OAuthAccessTokenResponse(accessToken);
//    OAuthMemberInfoResponse memberInfoResponse = new OAuthMemberInfoResponse(
//            oauthId, "saveuser", "https://github.com/save.jpg", ProviderType.GITHUB);
//
//    Member savedMember = Member.builder()
//            .id(100L)
//            .oauthId(oauthId)
//            .name("saveuser")
//            .profileUrl("https://github.com/save.jpg")
//            .providerType(ProviderType.GITHUB)
//            .build();
//
//    when(githubOAuthClient.getAccessToken(code)).thenReturn(tokenResponse);
//    when(githubOAuthClient.getMemberInfo(accessToken)).thenReturn(memberInfoResponse);
//    when(memberRepository.existsByOauthId(oauthId)).thenReturn(false);
//    when(memberRepository.findByOauthId(oauthId)).thenReturn(Optional.empty()); // 🔥 1번만 호출
//    when(memberRepository.save(any(Member.class))).thenReturn(savedMember);
//    when(tokenProvider.createTempToken(oauthId, "saveuser", ProviderType.GITHUB))
//            .thenReturn("jwt-save-token");
//
//    // when
//    SigninResponse response = oAuthService.signin(code, provider);
//
//    // then
//    assertThat(response.getToken()).isEqualTo("jwt-save-token");
//    assertThat(response.isNewUser()).isTrue();
//    assertThat(response.getName()).isEqualTo("saveuser");
//
//    // ArgumentCaptor로 저장된 Member 객체 검증
//    ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
//    verify(memberRepository).save(memberCaptor.capture());
//
//    Member capturedMember = memberCaptor.getValue();
//    assertThat(capturedMember.getOauthId()).isEqualTo(oauthId);
//    assertThat(capturedMember.getName()).isEqualTo("saveuser");
//    assertThat(capturedMember.getProfileUrl()).isEqualTo("https://github.com/save.jpg");
//    assertThat(capturedMember.getProviderType()).isEqualTo(ProviderType.GITHUB);
//  }
//}