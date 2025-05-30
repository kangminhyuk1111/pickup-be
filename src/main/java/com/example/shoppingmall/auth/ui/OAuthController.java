package com.example.shoppingmall.auth.ui;

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
    public ResponseEntity<?> gitHubCallback(@RequestParam String code) {
        return ResponseEntity.status(HttpStatus.CREATED).body(oAuthService.signin(code, "github"));
    }

    @GetMapping("/oauth/callback/google")
    public ResponseEntity<?> googleCallback(@RequestParam String code) {
        return ResponseEntity.status(HttpStatus.CREATED).body(oAuthService.signin(code, "google"));
    }

    @GetMapping("/oauth/callback/kakao")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code) {
        return ResponseEntity.status(HttpStatus.CREATED).body(oAuthService.signin(code, "kakao"));
    }
}
