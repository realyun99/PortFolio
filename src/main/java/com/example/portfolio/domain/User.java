package com.example.portfolio.domain;

import com.example.portfolio.enumerate.Role;
import com.example.portfolio.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Entity
@Table(name = "user", indexes = {@Index(columnList = "username"), @Index(columnList = "name"),
    @Index(columnList = "role"), @Index(columnList = "enabled"), @Index(columnList = "removed")})
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Integer id;

  @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "datetime default CURRENT_TIMESTAMP")
  @DateTimeFormat(pattern = DateUtil.PATTERN_YMDHM)
  private LocalDateTime createDate;   //생성일시

  @Size(min = 2, max = 32)
  @Column(unique = true, nullable = false, updatable = false)
  private String username;

  @Size(min = 2, max = 8)
  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  @JsonIgnore
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, updatable = false, columnDefinition = "varchar(50)")
  private Role role;

  @JsonIgnore
  @DateTimeFormat(pattern = DateUtil.PATTERN_YMD)
  @Column(columnDefinition = "date default CURRENT_TIMESTAMP")
  private LocalDate lastPasswordChangeDate = LocalDate.now();

  // 화면에서 추가인지 수정인지 여부를 보여주기 위함
  @Transient
  @JsonIgnore
  private boolean saved = false;

  @PostLoad
  private void postLoad() {
    this.saved = true;
  }

  @DateTimeFormat(pattern = DateUtil.PATTERN_YMD)
  private LocalDate expireDate;   //계정 만료일(null은 만료없음)

  @JsonIgnore
  @Column(columnDefinition = "bit(1) default 1", nullable = false)
  private boolean enabled = true;

  @JsonIgnore
  @Column(columnDefinition = "bit(1) default 0", nullable = false)
  private boolean locked = false;

  @JsonIgnore
  @Column(columnDefinition = "bit(1) default 0", nullable = false, insertable = false, updatable = false)
  private boolean superUser = false;

  @JsonIgnore
  @Column(columnDefinition = "bit(1) default 0", nullable = false, insertable = false)
  private boolean removed = false;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>(0);
    authorities.add(new SimpleGrantedAuthority(role.name()));
    return authorities;
  }

  /**
   * 신규 사용자이거나 비밀번호 란에 비밀번호를 입력한 경우 비밀번호 유효성 검사 필요
   */
  public boolean hasPasswordChanged() {
    try {
      return !password.isEmpty() || id == null;
    } catch (Exception e) {

    }
    return false;
  }

  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonExpired() {
//    if (expireDate != null) {
//      return expireDate.isAfter(LocalDate.now());
//    }
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return !locked && !removed;
  }

  @JsonIgnore
  @Override
  public boolean isEnabled() {
    return enabled && !removed;
  }

//  @JsonIgnore
//  public boolean hasRole(Role role) {
//    return this.role.equals(role);
//  }
}
