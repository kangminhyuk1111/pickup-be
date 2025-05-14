package com.example.shoppingmall.auth.application.client;

import com.example.shoppingmall.auth.application.dto.GoogleMemberInfoResponse;
import com.example.shoppingmall.auth.application.dto.GoogleOAuthAccessTokenRequest;
import com.example.shoppingmall.auth.application.dto.OAuthAccessTokenResponse;
import com.example.shoppingmall.auth.application.dto.OAuthMemberInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class GoogleOAuthClient implements OAuthClient {

    private static final String ACCESS_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String MEMBER_INFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";

    private final RestTemplate restTemplate;
    private final String clientId;
    private final String clientSecret;

    public GoogleOAuthClient(@Value("${google.client.id}") String clientId, @Value("${google.client.secret}") String clientSecret) {
        this.restTemplate = new RestTemplate();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public OAuthAccessTokenResponse getAccessToken(String code) {
        return restTemplate.postForObject(
                ACCESS_TOKEN_URL,
                new GoogleOAuthAccessTokenRequest(clientId, clientSecret, code),
                OAuthAccessTokenResponse.class);
    }

    @Override
    public OAuthMemberInfoResponse getMemberInfo(String accessToken) {
        HttpEntity<Void> request = createHttpEntityWithHeader(accessToken);

        return Objects.requireNonNull(restTemplate.exchange(
                MEMBER_INFO_URL,
                HttpMethod.GET,
                request,
                GoogleMemberInfoResponse.class
        ).getBody()).toOAuthMemberInfoResponse();
    }

    private HttpEntity<Void> createHttpEntityWithHeader(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        return new HttpEntity<>(headers);
    }
}
