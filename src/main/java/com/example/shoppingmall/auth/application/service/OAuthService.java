package com.example.shoppingmall.auth.application.service;

import com.example.shoppingmall.auth.application.client.OAuthClient;
import com.example.shoppingmall.auth.application.dto.OAuthAccessTokenResponse;
import com.example.shoppingmall.auth.application.dto.OAuthMemberInfoResponse;
import com.example.shoppingmall.auth.application.dto.SigninResponse;
import com.example.shoppingmall.auth.application.token.TokenProvider;
import com.example.shoppingmall.auth.domain.member.Member;
import com.example.shoppingmall.auth.domain.member.MemberRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class OAuthService {

  private final MemberRepository memberRepository;
  private final OAuthClient githubOAuthClient;
  private final OAuthClient googleOAuthClient;
  private final OAuthClient kakaoOauthClient;
  private final TokenProvider tokenProvider;

  public OAuthService(MemberRepository memberRepository,
      @Qualifier("githubOAuthClient") OAuthClient githubOAuthClient,
      @Qualifier("googleOAuthClient") OAuthClient googleOAuthClient,
      @Qualifier("kakaoOauthClient") OAuthClient kakaoOauthClient, TokenProvider tokenProvider) {
    this.memberRepository = memberRepository;
    this.githubOAuthClient = githubOAuthClient;
    this.googleOAuthClient = googleOAuthClient;
    this.kakaoOauthClient = kakaoOauthClient;
    this.tokenProvider = tokenProvider;
  }

  public SigninResponse signin(String code, String provider) {
    OAuthClient client = selectClient(provider);

    OAuthMemberInfoResponse memberInfoResponse = getOAuthMemberInfo(code, client);

    Member member = findOrSaveMember(memberInfoResponse);

    String token = tokenProvider.createToken(String.valueOf(member.getId()));

    return new SigninResponse(token);
  }

  private OAuthClient selectClient(String provider) {
    return switch (provider.toLowerCase()) {
      case "github" -> githubOAuthClient;
      case "google" -> googleOAuthClient;
      case "kakao" -> kakaoOauthClient;
      default -> throw new IllegalArgumentException("지원하지 않는 OAuth 제공자: " + provider);
    };
  }

  private OAuthMemberInfoResponse getOAuthMemberInfo(String code, OAuthClient client) {
      OAuthAccessTokenResponse tokenResponse = client.getAccessToken(code);

      if (tokenResponse == null) {
        throw new RuntimeException("토큰 응답이 null입니다");
      }

      String accessToken = tokenResponse.getAccessToken();

      if (accessToken == null || accessToken.isEmpty()) {
        throw new RuntimeException("액세스 토큰이 null이거나 비어있습니다");
      }

      return client.getMemberInfo(accessToken);
  }

  private Member findOrSaveMember(OAuthMemberInfoResponse memberResponse) {
    return memberRepository.findByOauthId(memberResponse.getOauthId())
        .orElseGet(() -> memberRepository.save(memberResponse.toMember()));
  }
}
