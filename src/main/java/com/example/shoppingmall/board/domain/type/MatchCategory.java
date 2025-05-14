package com.example.shoppingmall.board.domain.type;

import lombok.Getter;

@Getter
public enum MatchCategory {
  MATCHING("매칭"),
  FREE("자유"),
  NOTICE("공지");

  private final String description;

  MatchCategory(final String description) {
    this.description = description;
  }
}