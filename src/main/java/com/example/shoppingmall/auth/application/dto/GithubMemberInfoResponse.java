package com.example.shoppingmall.auth.application.dto;

import com.example.shoppingmall.auth.domain.type.ProviderType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GithubMemberInfoResponse {

  @JsonProperty("id")
  private String id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("avatar_url")
  private String avatarUrl;

  public OAuthMemberInfoResponse toOAuthMemberInfoResponse() {
    validateFields();
    return new OAuthMemberInfoResponse(
        id,
        name,
        Optional.ofNullable(avatarUrl).orElse(getDefaultProfileUrl()),
        ProviderType.GITHUB
    );
  }

  private void validateFields() {
    Objects.requireNonNull(id, "id 는 null이 될 수 없습니다.");
    Objects.requireNonNull(name, "name 은 null이 될 수 없습니다.");
  }

  private String getDefaultProfileUrl() {
    // GitHub 기본 identicon 사용
    return "https://github.com/identicons/" + id + ".png";
  }
}