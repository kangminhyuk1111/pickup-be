package com.example.shoppingmall.auth.domain.member;

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

  @Column(name = "oauth_id", nullable = false, unique = true)
  private String oauthId;

  @Column(nullable = false)
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

  public void updateProfile(String name, String profileUrl) {
    this.name = name;
    this.profileUrl = profileUrl;
  }

  @Override
  public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) {
          return false;
      }
    Member member = (Member) o;
    return Objects.equals(oauthId, member.oauthId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(oauthId);
  }
}