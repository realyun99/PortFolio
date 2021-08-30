package com.example.portfolio.config;

import com.example.portfolio.util.SpringSecurity;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CustomAuthenticationHandler
    implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication auth) throws IOException {

    response.sendRedirect(request.getContextPath() + SpringSecurity.LOGIN_SUCCESS_URL);
  }

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {

    String username = request.getParameter("username");
    String dispatcherURL = SpringSecurity.LOGIN_URL;

    request.setAttribute("SPRING_SECURITY_LAST_EXCEPTION", exception);
    request.setAttribute("SPRING_SECURITY_LAST_USERNAME", username);
    request.setAttribute("username", username);
    request.setAttribute("error", getErrorMessage(exception));
    request.getRequestDispatcher(dispatcherURL).forward(request, response);
  }

  /**
   * Spring Security 가 반환하는 에러 메시지 얻어옴
   *
   * @return 에러메시지
   */
  private String getErrorMessage(AuthenticationException exception) {

    String error;
    if (exception instanceof AccountExpiredException) {
      error = "사용 기간이 만료된 계정입니다.";
    } else if (exception instanceof BadCredentialsException) {
      error = "아이디 또는 비밀번호를 확인해주시기 바랍니다.";
    } else if (exception instanceof LockedException) {
      error = "로그인 반복 실패 또는 관리자에 의해 잠겨있는 계정입니다.\n관리자에게 문의하세요.";
    } else if (exception instanceof InternalAuthenticationServiceException) {
      error = "존재하지 않는 계정입니다.";
    } else if (exception instanceof DisabledException) {
      error = "승인되지 않은 계정입니다. 관리자의 승인을 기다려주세요.";
    } else if (exception instanceof CredentialsExpiredException) {
      error = "만료된 비밀번호 입니다.";
    } else {
      error = exception.getLocalizedMessage();
    }

    return error;
  }
}
