package com.example.shoppingmall.auth.application.dto;

import com.example.shoppingmall.auth.domain.member.Member;
import com.example.shoppingmall.auth.domain.type.ProviderType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OAuthMemberInfoResponse {

  @JsonProperty("id")
  private String oauthId;

  @JsonProperty("name")
  private String name;

  @JsonProperty("avatar_url")
  private String profileUrl;

  @JsonProperty("providerType")
  private ProviderType providerType;

  public Member toMember() {
    return Member.builder()
        .oauthId(oauthId)
        .name(name)
        .profileUrl(profileUrl)
        .providerType(providerType)
        .build();
  }
}
