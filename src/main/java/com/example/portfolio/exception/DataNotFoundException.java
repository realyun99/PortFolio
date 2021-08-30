package com.example.portfolio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class DataNotFoundException extends HttpClientErrorException {

  public DataNotFoundException() {
    super(HttpStatus.NOT_FOUND, "데이터를 찾을 수 없습니다.");
  }

  public DataNotFoundException(String message) {
    super(HttpStatus.NOT_FOUND, message);
  }
}
