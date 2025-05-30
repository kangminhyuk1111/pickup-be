package com.example.shoppingmall.auth.util;

import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class AuthUrlCreator {

    private static final String REDIRECT_URI = "http://localhost:8080/api/auth/oauth/callback";

    public String githubAuthUrl(final String githubClientId) {
        String redirectUri = REDIRECT_URI + "/github";
        String scope = "user:email";

        String authUrl = "https://github.com/login/oauth/authorize" +
                "?client_id=" + URLEncoder.encode(githubClientId, StandardCharsets.UTF_8) +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8);

        return "redirect:" + authUrl;
    }

    public String googleAuthUrl(final String googleClientId) {
        String redirectUri = REDIRECT_URI + "/google";
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
        String redirectUri = REDIRECT_URI + "/kakao";

        String authUrl = "https://kauth.kakao.com/oauth/authorize" +
            "?client_id=" + URLEncoder.encode(kakaoClientId, StandardCharsets.UTF_8) +
            "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
            "&response_type=code";

        return "redirect:" + authUrl;
    }
}
