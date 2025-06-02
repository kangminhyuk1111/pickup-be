package com.example.shoppingmall.auth.application.dto;

import com.example.shoppingmall.auth.domain.member.Member;
import com.example.shoppingmall.auth.domain.type.Position;

public record MemberResponse(
    Long id,
    String name,
    Position position,
    String profileUrl
) {
  public static MemberResponse from(Member member) {
    return new MemberResponse(
        member.getId(),
        member.getName(),
        member.getPosition(),
        member.getProfileUrl()
    );
  }
}