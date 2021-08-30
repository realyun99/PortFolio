package com.example.portfolio.repository.support;

import com.example.portfolio.domain.QUser;
import com.example.portfolio.domain.User;
import com.example.portfolio.filter.UserFilter;
import com.example.portfolio.repository.custom.CustomUserRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class UserRepositoryImpl extends QuerydslRepositorySupport implements CustomUserRepository {

  private QUser user = QUser.user;

  public UserRepositoryImpl() {
    super(User.class);
  }

  @Override
  public Page<User> findAllByFilter(Pageable pageable, UserFilter filter) {
    BooleanBuilder builder = new BooleanBuilder();

    builder.and(user.removed.isFalse());

    if (filter.getFRole() != null) {
      builder.and(user.role.eq(filter.getFRole()));
    }

    if (!StringUtils.isBlank(filter.getFName())) {
      builder.and(user.name.containsIgnoreCase(filter.getFName()));
    }

    if (!StringUtils.isBlank(filter.getFUsername())) {
      builder.and(user.username.containsIgnoreCase(filter.getFUsername()));
    }

    if (filter.getFLocked() != null) {
      builder.and(user.locked.eq(filter.getFLocked()));
    }

    final JPQLQuery<User> query = from(user).where(builder);
    final List<User> result = getQuerydsl().applyPagination(pageable, query).fetch();
    return new PageImpl<>(result, pageable, query.fetchCount());
  }
}

