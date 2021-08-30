package com.example.portfolio.config;

import com.example.portfolio.util.SpringSecurity;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override

  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication instanceof AnonymousAuthenticationToken) {
      response.sendRedirect(request.getContextPath() + SpringSecurity.LOGIN_URL);
    }
  }
}
