package com.example.portfolio.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleBase {
  ADMINISTRATOR("관리자"),
  USER("사용자");

  private String text;
}
