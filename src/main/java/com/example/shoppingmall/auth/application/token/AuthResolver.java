package com.example.shoppingmall.auth.application.token;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthResolver implements HandlerMethodArgumentResolver {

  public static final String BEARER_TYPE = "Bearer";
  public static final String AUTHORIZATION = "Authorization";

  private final TokenProvider tokenProvider;

  public AuthResolver(TokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(Auth.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
    HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

    String token = extractToken(request);

    // 토큰 유효성 검증 추가
    tokenProvider.validateToken(token);

    String payload = tokenProvider.parsePayload(token);
    return Long.parseLong(payload);
  }

  private String extractToken(HttpServletRequest request) {
    String value = request.getHeader(AUTHORIZATION);
    System.out.println("Authorization 헤더 값: [" + value + "]"); // 디버그 로그

    if (value == null || value.trim().isEmpty()) {
      throw new IllegalArgumentException("Authorization 헤더가 없거나 비어있습니다.");
    }

    value = value.trim();
    System.out.println("trim 후 값: [" + value + "]"); // 디버그 로그

    if (!value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
      throw new IllegalArgumentException("Bearer 토큰이 아닙니다. 현재 값: " + value);
    }

    String token = value.substring(BEARER_TYPE.length()).trim();
    System.out.println("추출된 토큰: [" + token + "]"); // 디버그 로그

    int commaIndex = token.indexOf(',');
    if (commaIndex > 0) {
      token = token.substring(0, commaIndex).trim();
      System.out.println("콤마 처리 후 토큰: [" + token + "]"); // 디버그 로그
    }

    if (token.isEmpty()) {
      throw new IllegalArgumentException("Bearer 토큰이 비어있습니다.");
    }

    return token;
  }
}