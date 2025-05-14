package com.example.shoppingmall.auth.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleOAuthAccessTokenRequest {

  @JsonProperty("client_id")
  private String clientId;

  @JsonProperty("client_secret")
  private String clientSecret;

  private String code;

  @JsonProperty("grant_type")
  private String grantType = "authorization_code";  // 추가!

  @JsonProperty("redirect_uri")
  private String redirectUri;

  public GoogleOAuthAccessTokenRequest(String clientId, String clientSecret, String code) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.code = code;
    this.grantType = "authorization_code";
    this.redirectUri = "http://localhost:8080/api/auth/oauth/callback/google";
  }
}