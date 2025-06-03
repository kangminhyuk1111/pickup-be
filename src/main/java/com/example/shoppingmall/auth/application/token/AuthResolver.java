package com.example.shoppingmall.auth.application.token;

import com.example.shoppingmall.core.exception.AuthorizationException;
import com.example.shoppingmall.core.exception.CustomErrorCode;
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

    tokenProvider.validateToken(token);

    String payload = tokenProvider.parsePayload(token);

    return Long.parseLong(payload);
  }

  private String extractToken(HttpServletRequest request) {
    String authHeader = request.getHeader(AUTHORIZATION);

    if (authHeader == null || authHeader.trim().isEmpty()) {
      throw new AuthorizationException(CustomErrorCode.TOKEN_NOT_FOUND);
    }

    authHeader = authHeader.trim();

    if (!authHeader.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
      throw new AuthorizationException(CustomErrorCode.INVALID_TOKEN);
    }

    String token = authHeader.substring(BEARER_TYPE.length()).trim();

    int commaIndex = token.indexOf(',');
    if (commaIndex > 0) {
      token = token.substring(0, commaIndex).trim();
    }

    if (token.isEmpty()) {
      throw new AuthorizationException(CustomErrorCode.BEARER_NOT_FOUND);
    }

    return token;
  }
}