package com.example.shoppingmall.auth.application.dto;

import com.example.shoppingmall.auth.domain.member.Member;
import com.example.shoppingmall.auth.domain.type.Position;
import com.example.shoppingmall.auth.domain.type.ProviderType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponse {

  private String token;
  private MemberDto member;

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class MemberDto {
    private String email;
    private String nickname;
    private Position position;
    private ProviderType provider;

    public MemberDto(final Member member) {
      this.email = member.getEmail();
      this.nickname = member.getName();
      this.position = member.getPosition();
      this.provider = member.getProviderType();
    }
  }
}
