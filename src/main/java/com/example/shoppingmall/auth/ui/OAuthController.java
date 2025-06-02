package com.example.shoppingmall.auth.ui;

import com.example.shoppingmall.auth.application.dto.SignUpRequest;
import com.example.shoppingmall.auth.application.dto.SignUpResponse;
import com.example.shoppingmall.auth.application.dto.SigninResponse;
import com.example.shoppingmall.auth.application.service.OAuthService;
import com.example.shoppingmall.auth.util.AuthUrlCreator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/auth")
public class OAuthController {

    public static final String GITHUB = "github";
    public static final String GOOGLE = "google";
    public static final String KAKAO = "kakao";

    private final String githubClientId;
    private final String googleClientId;
    private final String kakaoClientId;

    private final OAuthService oAuthService;
    private final AuthUrlCreator authUrlCreator;

    public OAuthController(
            @Value("${github.client.id}") String githubClientId,
            @Value("${google.client.id}") String googleClientId,
            @Value("${kakao.client.id}") String kakaoClientId,
            OAuthService oAuthService,
            AuthUrlCreator authUrlCreator) {
        this.githubClientId = githubClientId;
        this.googleClientId = googleClientId;
        this.kakaoClientId = kakaoClientId;
        this.oAuthService = oAuthService;
        this.authUrlCreator = authUrlCreator;
    }

    @GetMapping("/login/github")
    public String loginWithGitHub() {
        return authUrlCreator.githubAuthUrl(githubClientId);
    }

    @GetMapping("/login/google")
    public String loginWithGoogle() {
        return authUrlCreator.googleAuthUrl(googleClientId);
    }

    @GetMapping("/login/kakao")
    public String loginWithKakao() {
        return authUrlCreator.kakaoAuthUrl(kakaoClientId);
    }

    @GetMapping("/oauth/callback/github")
    public String gitHubCallback(@RequestParam String code) {
        final SigninResponse response = oAuthService.signin(code, "github");

        return "redirect:" + buildRedirectUrl(GITHUB, response);
    }

    @GetMapping("/oauth/callback/google")
    public String googleCallback(@RequestParam String code) {
        final SigninResponse response = oAuthService.signin(code, "google");

        return "redirect:" + buildRedirectUrl(GOOGLE, response);
    }

    @GetMapping("/oauth/callback/kakao")
    public String kakaoCallback(@RequestParam String code) {
        final SigninResponse response = oAuthService.signin(code, "kakao");

        return "redirect:" + buildRedirectUrl(KAKAO, response);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request, @RequestHeader("Authorization") String authorization) {
        final String tempToken = authorization.replace("Bearer ", "");

        final SignUpResponse signUpResponse = oAuthService.signUp(request, tempToken);
        return ResponseEntity.ok(signUpResponse);
    }

    private String buildRedirectUrl(String provider, SigninResponse response) {
      return "http://localhost:3000/auth/callback/" + provider + "?token=" + response.getToken() + "&isNewUser=" + response.isNewUser();
    }
}
