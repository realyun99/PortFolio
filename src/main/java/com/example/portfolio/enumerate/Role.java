package com.example.portfolio.enumerate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
  ROLE_ADMIN("관리자", RoleBase.ADMINISTRATOR),
  ROLE_USER("사용자", RoleBase.USER);

  private String text;
  private RoleBase roleBase;

  public static List<Role> findByRoleBase(RoleBase roleBase) {
    return Arrays.stream(Role.values()).filter(p -> p.getRoleBase().equals(roleBase))
        .collect(Collectors.toList());
  }
}
