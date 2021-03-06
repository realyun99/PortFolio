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
  private LocalDateTime createDate;   //μμ±μΌμ

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

  // νλ©΄μμ μΆκ°μΈμ§ μμ μΈμ§ μ¬λΆλ₯Ό λ³΄μ¬μ£ΌκΈ° μν¨
  @Transient
  @JsonIgnore
  private boolean saved = false;

  @PostLoad
  private void postLoad() {
    this.saved = true;
  }

  @DateTimeFormat(pattern = DateUtil.PATTERN_YMD)
  private LocalDate expireDate;   //κ³μ  λ§λ£μΌ(nullμ λ§λ£μμ)

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
   * μ κ· μ¬μ©μμ΄κ±°λ λΉλ°λ²νΈ λμ λΉλ°λ²νΈλ₯Ό μλ ₯ν κ²½μ° λΉλ°λ²νΈ μ ν¨μ± κ²μ¬ νμ
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
