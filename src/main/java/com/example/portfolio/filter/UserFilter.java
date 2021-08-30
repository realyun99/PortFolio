package com.example.portfolio.filter;

import com.example.portfolio.enumerate.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserFilter extends QueryStringSupport {

  private Role fRole;

  private String fUsername;

  private String fName;

  private Boolean fLocked;

}
