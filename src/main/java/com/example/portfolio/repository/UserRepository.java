package com.example.portfolio.repository;

import com.example.portfolio.repository.custom.CustomUserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.portfolio.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, CustomUserRepository {

  Optional<User> findByUsername(String username);

  boolean existsByUsername(String username);

  boolean existsByName(String name);

  @Query("SELECT p.password FROM User p WHERE p.id = :#{#user.id}")
  String findPasswordByUser(@Param("user") User user);

  @Modifying
  @Query("UPDATE User p SET p.removed = true, p.username = CONCAT(p.username, '-removed') WHERE p.id IN(:ids)")
  int removeByIdIn(@Param("ids") List<Integer> ids);

  Page<User> findAllByIdIn(List<Integer> ids, Pageable pageable);
}
