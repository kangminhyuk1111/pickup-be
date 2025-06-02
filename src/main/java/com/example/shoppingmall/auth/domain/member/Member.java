package com.example.shoppingmall.auth.domain.member;

import com.example.shoppingmall.auth.application.dto.SignUpRequest;
import com.example.shoppingmall.auth.domain.type.Position;
import com.example.shoppingmall.auth.domain.type.Role;
import com.example.shoppingmall.auth.domain.type.ProviderType;
import com.example.shoppingmall.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Entity
@Table(name = "members")
@Getter
public class Member extends BaseEntity {

  @Column(name = "email", unique = true)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(name = "position")
  private Position position;

  @Column(name = "oauth_id", nullable = false, unique = true)
  private String oauthId;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "profile_url")
  private String profileUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role = Role.USER;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ProviderType providerType;

  public Member() {
  }

  @Builder
  public Member(Long id, String oauthId, String name, String profileUrl,
      ProviderType providerType) {
    super(id);
    this.oauthId = oauthId;
    this.name = name;
    this.profileUrl = profileUrl;
    this.providerType = providerType;
  }

  public void updateProfileUrl(String name, String profileUrl) {
    this.name = name;
    this.profileUrl = profileUrl;
  }

  public void updateSignUp(SignUpRequest request) {
    this.email = request.getEmail();
    this.name = request.getNickname();
    this.providerType = request.getProvider();
    this.position = request.getPosition();
  }
}