package com.example.shoppingmall.auth.application.dto;

import com.example.shoppingmall.auth.domain.type.ProviderType;
import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoMemberInfoResponse(
    Long id,
    @JsonProperty("connected_at") String connectedAt,
    @JsonProperty("kakao_account") KakaoAccount kakaoAccount
) {
  public OAuthMemberInfoResponse toOAuthMemberInfoResponse() {
    String nickname = kakaoAccount != null && kakaoAccount.profile() != null
        ? kakaoAccount.profile().nickname()
        : "카카오사용자";
    String profileImageUrl = kakaoAccount != null && kakaoAccount.profile() != null
        ? kakaoAccount.profile().profileImageUrl()
        : "";

    return new OAuthMemberInfoResponse(
        String.valueOf(id),
        nickname,
        profileImageUrl,
        ProviderType.KAKAO
    );
  }

  public record KakaoAccount(
      @JsonProperty("profile_nickname_needs_agreement") Boolean profileNicknameNeedsAgreement,
      @JsonProperty("profile_image_needs_agreement") Boolean profileImageNeedsAgreement,
      Profile profile,
      @JsonProperty("has_email") Boolean hasEmail,
      @JsonProperty("email_needs_agreement") Boolean emailNeedsAgreement,
      @JsonProperty("is_email_valid") Boolean isEmailValid,
      @JsonProperty("is_email_verified") Boolean isEmailVerified,
      String email
  ) {}

  public record Profile(
      String nickname,
      @JsonProperty("thumbnail_image_url") String thumbnailImageUrl,
      @JsonProperty("profile_image_url") String profileImageUrl,
      @JsonProperty("is_default_image") Boolean isDefaultImage
  ) {}
}