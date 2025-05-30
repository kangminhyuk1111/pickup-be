package com.example.shoppingmall.auth.ui;

import com.example.shoppingmall.auth.application.dto.SigninResponse;
import com.example.shoppingmall.auth.application.service.OAuthService;
import com.example.shoppingmall.auth.util.AuthUrlCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/auth")
public class OAuthController {

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
        final SigninResponse githubResponse = oAuthService.signin(code, "github");
        return "redirect:http://localhost:3000/auth/callback/github?token=" + githubResponse.getToken();
    }

    @GetMapping("/oauth/callback/google")
    public String googleCallback(@RequestParam String code) {
        final SigninResponse googleResponse = oAuthService.signin(code, "google");
        return "redirect:http://localhost:3000/auth/callback/google?token=" + googleResponse.getToken();
    }

    @GetMapping("/oauth/callback/kakao")
    public String kakaoCallback(@RequestParam String code) {
        final SigninResponse kakaoResponse = oAuthService.signin(code, "kakao");
        return "redirect:http://localhost:3000/auth/callback/kakao?token=" + kakaoResponse.getToken();
    }
}
