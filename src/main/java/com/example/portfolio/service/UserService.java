package com.example.portfolio.service;

import com.example.portfolio.domain.User;
import com.example.portfolio.exception.DataNotFoundException;
import com.example.portfolio.exception.UserPasswordValidationException;
import com.example.portfolio.filter.UserFilter;
import com.example.portfolio.repository.UserRepository;
import com.example.portfolio.util.PasswordUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  @NonNull
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  @NonNull
  private final UserRepository userRepository;

  public Optional<User> findById(Integer id) {
    return userRepository.findById(id);
  }

  public Page<User> findAllByFilter(Pageable pageable, UserFilter filter) {
    return userRepository.findAllByFilter(pageable, filter);
  }

  public User save(User user) throws UserPasswordValidationException, DataNotFoundException {
    if (user.hasPasswordChanged()) {
      if (PasswordUtil.isValidPassword(user.getPassword())) {
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setLastPasswordChangeDate(LocalDate.now());
      } else {
        throw new UserPasswordValidationException();
      }
    } else {
      user.setPassword(userRepository.findPasswordByUser(user));
    }
    return userRepository.save(user);
  }

  @Transactional
  public int removeByIdIn(List<Integer> ids) {
    return userRepository.removeByIdIn(ids);
  }

  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByUsername(username);
    if (!user.isPresent()) {
      throw new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
    }
    return user.get();
  }

  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  public boolean existsByName(String name) {
    return userRepository.existsByName(name);
  }

  public Page<User> findAllByIdIn(List<Integer> ids, Pageable pageable) {
    return userRepository.findAllByIdIn(ids, pageable);
  }
}
