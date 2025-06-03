package com.example.shoppingmall;

import com.example.shoppingmall.auth.config.TestAuthConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Import(TestAuthConfig.class)
class ShopApplicationTests {

	@Test
	void contextLoads() {
	}

}
