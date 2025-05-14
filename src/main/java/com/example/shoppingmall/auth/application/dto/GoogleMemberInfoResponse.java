package com.example.shoppingmall.auth.application.dto;

import com.example.shoppingmall.auth.domain.type.ProviderType;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleMemberInfoResponse {

  private String id;
  private String name;
  private String picture;

  public OAuthMemberInfoResponse toOAuthMemberInfoResponse() {
    validateFields();
    return new OAuthMemberInfoResponse(id, name, picture, ProviderType.GOOGLE);
  }

  private void validateFields() {
    Objects.requireNonNull(id, "id 는 null이 될 수 없습니다.");
    Objects.requireNonNull(name, "name 은 null이 될 수 없습니다.");
    Objects.requireNonNull(picture, "picture 은 null이 될 수 없습니다.");
  }
}
