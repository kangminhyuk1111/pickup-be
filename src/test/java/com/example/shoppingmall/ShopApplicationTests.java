package com.example.shoppingmall;

import com.example.shoppingmall.auth.config.TestAuthConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
		"KAKAO_CLIENT_ID=test-kakao-client-id",
		"KAKAO_CLIENT_SECRET=test-kakao-client-secret",
		"KAKAO_REDIRECT_URI=http://localhost:8080/test/auth/callback/kakao",

		"OAUTH_GITHUB_CLIENT_ID=test-github-client-id",
		"OAUTH_GITHUB_CLIENT_SECRET=test-github-client-secret",
		"OAUTH_GITHUB_REDIRECT_URI=http://localhost:8080/test/auth/callback/github",

		"GOOGLE_CLIENT_ID=test-google-client-id",
		"GOOGLE_CLIENT_SECRET=test-google-client-secret",
		"GOOGLE_REDIRECT_URI=http://localhost:8080/test/auth/callback/google",

		"JWT_SECRET_KEY=testSecretKeyForTestingPurposesOnly123456789012345678901234567890"
})
@Import(TestAuthConfig.class)
class ShopApplicationTests {

	@Test
	void contextLoads() {
	}

}
