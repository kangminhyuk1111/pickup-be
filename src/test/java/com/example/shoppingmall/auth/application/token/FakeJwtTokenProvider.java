package com.example.shoppingmall.auth.application.token;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class FakeJwtTokenProvider extends JwtTokenProvider{

  public FakeJwtTokenProvider() {
    super("test123456678123182398491928cdjcncn18238182674912n3anwasdjfkjhcvjk1123123", 3600000L);
  }
}
