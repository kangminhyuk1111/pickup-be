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
  private final TokenProvider tokenProvider;

  public OAuthService(MemberRepository memberRepository,
      @Qualifier("githubOAuthClient") OAuthClient githubOAuthClient,
      @Qualifier("googleOAuthClient") OAuthClient googleOAuthClient, TokenProvider tokenProvider) {
    this.memberRepository = memberRepository;
    this.githubOAuthClient = githubOAuthClient;
    this.googleOAuthClient = googleOAuthClient;
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
      default -> throw new IllegalArgumentException("지원하지 않는 OAuth 제공자: " + provider);
    };
  }

  private OAuthMemberInfoResponse getOAuthMemberInfo(String code, OAuthClient client) {
    try {
      OAuthAccessTokenResponse tokenResponse = client.getAccessToken(code);
      return client.getMemberInfo(tokenResponse.getAccessToken());
    } catch (HttpClientErrorException e) {
      throw new RuntimeException(code + "를 이용해서 OAuth 정보를 받아오는 데 실패했습니다.");
    }
  }

  private Member findOrSaveMember(OAuthMemberInfoResponse memberResponse) {
    return memberRepository.findByOauthId(memberResponse.getOauthId())
        .orElseGet(() -> memberRepository.save(memberResponse.toMember()));
  }
}
