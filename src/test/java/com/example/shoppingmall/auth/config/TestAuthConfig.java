package com.example.shoppingmall.auth.config;

import com.example.shoppingmall.auth.application.token.FakeJwtTokenProvider;
import com.example.shoppingmall.auth.application.token.JwtTokenProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestAuthConfig {

  @Bean
  @Primary  // 실제 JwtTokenProvider 대신 이 구현체가 주입되도록
  public JwtTokenProvider jwtTokenProvider() {
    return new FakeJwtTokenProvider();
  }
}