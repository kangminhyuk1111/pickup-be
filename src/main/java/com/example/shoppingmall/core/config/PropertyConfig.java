package com.example.shoppingmall.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
public class PropertyConfig {

  @Configuration
  @Profile("local")
  @PropertySources({
      @PropertySource(value = "classpath:.env.local", ignoreResourceNotFound = true),
  })
  public static class LocalConfig {

  }

  @Configuration
  @Profile("prod")
  @PropertySources({
      @PropertySource(value = "classpath:.env.production", ignoreResourceNotFound = true),
  })
  public static class ProdConfig {

  }
}
