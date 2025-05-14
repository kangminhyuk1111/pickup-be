package com.example.shoppingmall.auth.application.client;

import com.example.shoppingmall.auth.application.dto.GithubMemberInfoResponse;
import com.example.shoppingmall.auth.application.dto.GithubOAuthAccessTokenRequest;
import com.example.shoppingmall.auth.application.dto.OAuthAccessTokenResponse;
import com.example.shoppingmall.auth.application.dto.OAuthMemberInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import java.util.Objects;

@Component
public class GithubOAuthClient implements OAuthClient {

  private static final String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
  private static final String MEMBER_INFO_URL = "https://api.github.com/user";

  private final RestTemplate restTemplate;
  private final String clientId;
  private final String clientSecret;

  public GithubOAuthClient(@Value("${github.client.id}") String clientId,
      @Value("${github.client.secret}") String clientSecret) {
    this.restTemplate = new RestTemplate();
    this.clientId = clientId;
    this.clientSecret = clientSecret;
  }

  @Override
  public OAuthAccessTokenResponse getAccessToken(String code) {
    return restTemplate.postForObject(
        ACCESS_TOKEN_URL,
        new GithubOAuthAccessTokenRequest(clientId, clientSecret, code),
        OAuthAccessTokenResponse.class);
  }

  @Override
  public OAuthMemberInfoResponse getMemberInfo(String accessToken) {
    HttpEntity<Void> request = createHttpEntityWithHeader(accessToken);

    return Objects.requireNonNull(restTemplate.exchange(
        MEMBER_INFO_URL,
        HttpMethod.GET,
        request,
        GithubMemberInfoResponse.class
    ).getBody()).toOAuthMemberInfoResponse();
  }

  private HttpEntity<Void> createHttpEntityWithHeader(String accessToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    return new HttpEntity<>(headers);
  }
}
