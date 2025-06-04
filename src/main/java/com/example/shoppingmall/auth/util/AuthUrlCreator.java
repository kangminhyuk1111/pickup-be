package com.example.shoppingmall.auth.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class AuthUrlCreator {

  private final String redirectUrl;

  public AuthUrlCreator(@Value("${oauth.callback.url}") final String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  public String githubAuthUrl(final String githubClientId) {
    String redirectUri = redirectUrl + "/github";
    String scope = "user:email";

    String authUrl = "https://github.com/login/oauth/authorize" +
        "?client_id=" + URLEncoder.encode(githubClientId, StandardCharsets.UTF_8) +
        "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
        "&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8);

    return "redirect:" + authUrl;
  }

  public String googleAuthUrl(final String googleClientId) {
    String redirectUri = redirectUrl + "/google";
    String scope = "profile email openid";

    String authUrl = "https://accounts.google.com/o/oauth2/v2/auth" +
        "?client_id=" + URLEncoder.encode(googleClientId, StandardCharsets.UTF_8) +
        "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
        "&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8) +
        "&response_type=code" +
        "&access_type=offline";

    return "redirect:" + authUrl;
  }

  public String kakaoAuthUrl(final String kakaoClientId) {
    String redirectUri = redirectUrl + "/kakao";

    String authUrl = "https://kauth.kakao.com/oauth/authorize" +
        "?client_id=" + URLEncoder.encode(kakaoClientId, StandardCharsets.UTF_8) +
        "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
        "&response_type=code";

    return "redirect:" + authUrl;
  }
}
