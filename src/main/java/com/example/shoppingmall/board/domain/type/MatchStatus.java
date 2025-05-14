package com.example.shoppingmall.board.domain.type;

import lombok.Getter;

@Getter
public enum MatchStatus {
  RECRUITING("모집중"),
  COMPLETED("모집완료"),
  FINISHED("경기완료");

  private final String description;

  MatchStatus(final String description) {
    this.description = description;
  }
}
