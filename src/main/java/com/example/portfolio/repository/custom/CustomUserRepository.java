package com.example.portfolio.repository.custom;

import com.example.portfolio.domain.User;
import com.example.portfolio.filter.UserFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomUserRepository {

  Page<User> findAllByFilter(Pageable pageable, UserFilter filter);

}