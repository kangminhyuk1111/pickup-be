package com.example.shoppingmall.auth.application.token;

import com.example.shoppingmall.auth.domain.type.ProviderType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider implements TokenProvider {

  private final SecretKey key;
  private final Long validityInMilliseconds;

  public JwtTokenProvider(@Value("${security.jwt.token.secret-key}") String secretKey,
      @Value("${security.jwt.token.expire-length}") Long validityInMilliseconds) {
    this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    this.validityInMilliseconds = validityInMilliseconds;
  }

  @Override
  public String createToken(String payload) {
    validatePayload(payload);

    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder().setSubject(payload).setIssuedAt(now).setExpiration(validity)
        .signWith(key, SignatureAlgorithm.HS256).compact();
  }

  @Override
  public String parsePayload(String token) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody()
          .getSubject();
    } catch (JwtException | IllegalArgumentException e) {
      throw new RuntimeException(token + "은 유효하지 않은 토큰입니다.");
    }
  }

  @Override
  public Long getUserId(final String token) {
    String userIdString = parsePayload(token);
    try {
      return Long.valueOf(userIdString);
    } catch (NumberFormatException e) {
      throw new RuntimeException("토큰에서 유효한 사용자 ID를 추출할 수 없습니다: " + userIdString);
    }
  }

  @Override
  public void validateToken(String token) {
    validateNullToken(token);
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    } catch (ExpiredJwtException e) {
      throw new RuntimeException(token + "은 만료된 토큰입니다.");
    } catch (JwtException | IllegalArgumentException e) {
      throw new RuntimeException(token + "은 유효하지 않은 토큰입니다.");
    }
  }

  @Override
  public String createTempToken(final String oauthId, final String nickname,
      final ProviderType provider) {
    Claims claims = Jwts.claims();
    claims.put("oauthId", oauthId);
    claims.put("nickname", nickname);
    claims.put("provider", provider);
    claims.put("type", "temp");

    return Jwts.builder().setClaims(claims)
        .setExpiration(new Date(System.currentTimeMillis() + 1800000))
        .signWith(key, SignatureAlgorithm.HS256).compact();
  }

  @Override
  public Claims getClaims(String token) {
    validateNullToken(token);

    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private void validatePayload(final String payload) {
    Objects.requireNonNull(payload, "payload is not null");
  }

  private void validateNullToken(final String token) {
    Objects.requireNonNull(token, "payload is not null");
  }
}
