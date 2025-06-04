package com.example.shoppingmall.auth.application.client;

import com.example.shoppingmall.auth.application.dto.KakaoMemberInfoResponse;
import com.example.shoppingmall.auth.application.dto.OAuthAccessTokenResponse;
import com.example.shoppingmall.auth.application.dto.OAuthMemberInfoResponse;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoOauthClient implements OAuthClient {


  private static final String ACCESS_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
  private static final String MEMBER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

  private final RestTemplate restTemplate;
  private final String clientId;
  private final String clientSecret;
  private final String redirectUrl;

  public KakaoOauthClient(@Value("${kakao.client.id}") String clientId,
      @Value("${kakao.client.secret}") String clientSecret,
      @Value("${kakao.redirect.uri}") final String redirectUrl) {
    this.restTemplate = new RestTemplate();
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.redirectUrl = redirectUrl;
  }

  @Override
  public OAuthAccessTokenResponse getAccessToken(String code) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", "authorization_code");
    params.add("client_id", clientId);
    params.add("client_secret", clientSecret);
    params.add("redirect_uri", redirectUrl);
    params.add("code", code);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

    return restTemplate.postForObject(
        ACCESS_TOKEN_URL,
        request,
        OAuthAccessTokenResponse.class);
  }

  @Override
  public OAuthMemberInfoResponse getMemberInfo(String accessToken) {
    HttpEntity<Void> request = createHttpEntityWithHeader(accessToken);

    return Objects.requireNonNull(restTemplate.exchange(
            MEMBER_INFO_URL,
            HttpMethod.GET,
            request,
            KakaoMemberInfoResponse.class
        ).getBody()).
        toOAuthMemberInfoResponse();
  }

  private HttpEntity<Void> createHttpEntityWithHeader(String accessToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    return new HttpEntity<>(headers);
  }
}
