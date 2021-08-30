package com.example.portfolio.exception;

@SuppressWarnings("serial")
public class UserPasswordValidationException extends Exception {

  @Override
  public String getMessage() {
    return "비밀번호 규칙이 일치하지 않습니다";
  }
}

