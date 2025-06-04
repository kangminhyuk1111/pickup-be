package com.example.shoppingmall.auth.application.service;

import com.example.shoppingmall.auth.application.client.OAuthClient;
import com.example.shoppingmall.auth.application.dto.OAuthAccessTokenResponse;
import com.example.shoppingmall.auth.application.dto.OAuthMemberInfoResponse;
import com.example.shoppingmall.auth.application.dto.SignUpRequest;
import com.example.shoppingmall.auth.application.dto.SignUpResponse;
import com.example.shoppingmall.auth.application.dto.SignUpResponse.MemberDto;
import com.example.shoppingmall.auth.application.dto.SigninResponse;
import com.example.shoppingmall.auth.application.token.TokenProvider;
import com.example.shoppingmall.auth.domain.member.Member;
import com.example.shoppingmall.auth.domain.member.MemberRepository;
import com.example.shoppingmall.auth.domain.type.ProviderType;
import com.example.shoppingmall.core.exception.AuthorizationException;
import com.example.shoppingmall.core.exception.CustomErrorCode;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class OAuthService {

  private final MemberRepository memberRepository;
//  private final OAuthClient githubOAuthClient;
  private final OAuthClient googleOAuthClient;
  private final OAuthClient kakaoOauthClient;
  private final TokenProvider tokenProvider;

  public OAuthService(MemberRepository memberRepository,
//      @Qualifier("githubOAuthClient") OAuthClient githubOAuthClient,
      @Qualifier("googleOAuthClient") OAuthClient googleOAuthClient,
      @Qualifier("kakaoOauthClient") OAuthClient kakaoOauthClient, TokenProvider tokenProvider) {
    this.memberRepository = memberRepository;
//    this.githubOAuthClient = githubOAuthClient;
    this.googleOAuthClient = googleOAuthClient;
    this.kakaoOauthClient = kakaoOauthClient;
    this.tokenProvider = tokenProvider;
  }

  @Transactional
  public SigninResponse signin(String code, String provider) {
    OAuthClient client = selectClient(provider);
    OAuthMemberInfoResponse memberInfoResponse = getOAuthMemberInfo(code, client);

    boolean isNewUser = !memberRepository.existsByOauthId(memberInfoResponse.getOauthId());

    Member member = findOrSaveMember(memberInfoResponse);

    String token;
    if (isNewUser) {
      token = tokenProvider.createTempToken(
          memberInfoResponse.getOauthId(),
          memberInfoResponse.getName(),
          memberInfoResponse.getProviderType()
      );
    } else {
      token = tokenProvider.createToken(String.valueOf(member.getId()));
    }

    return new SigninResponse(token, isNewUser, memberInfoResponse.getName());
  }

  @Transactional
  public SignUpResponse signUp(final SignUpRequest request, final String tempToken) {
    final Claims claims = tokenProvider.getClaims(tempToken);
    final String oauthId = claims.get("oauthId", String.class);
    final String provider = claims.get("provider", String.class);

    final ProviderType providerType = ProviderType.valueOf(provider);

    final Member member = memberRepository.findByOauthIdAndProviderType(oauthId, providerType)
        .orElseThrow(() -> new AuthorizationException(CustomErrorCode.USER_NOT_FOUND));

    member.updateSignUp(request);

    final String token = tokenProvider.createToken(String.valueOf(member.getId()));

    return new SignUpResponse(token, new MemberDto(member));
  }

  private OAuthClient selectClient(String provider) {
    return switch (provider.toLowerCase()) {
//      case "github" -> githubOAuthClient;
      case "google" -> googleOAuthClient;
      case "kakao" -> kakaoOauthClient;
      default -> throw new AuthorizationException(CustomErrorCode.INVALID_OAUTH_CLIENT);
    };
  }

  private OAuthMemberInfoResponse getOAuthMemberInfo(String code, OAuthClient client) {
    OAuthAccessTokenResponse tokenResponse = client.getAccessToken(code);

    if (tokenResponse == null) {
      throw new AuthorizationException(CustomErrorCode.INVALID_TOKEN);
    }

    String accessToken = tokenResponse.getAccessToken();

    if (accessToken == null || accessToken.isEmpty()) {
      throw new AuthorizationException(CustomErrorCode.TOKEN_NOT_FOUND);
    }

    return client.getMemberInfo(accessToken);
  }

  private Member findOrSaveMember(OAuthMemberInfoResponse memberResponse) {
    return memberRepository.findByOauthId(memberResponse.getOauthId())
        .orElseGet(() -> memberRepository.save(memberResponse.toMember()));
  }
}
