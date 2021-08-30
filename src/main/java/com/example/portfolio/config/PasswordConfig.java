package com.example.portfolio.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Getter
@Setter
@Configuration
@ConfigurationProperties("password")
@PropertySources({@PropertySource(value = "classpath:/config/password.properties")})
public class PasswordConfig {

  private Validation validation;

  @Getter
  @Setter
  @Configuration
  @ConfigurationProperties("herbnet.password-validation")
  public static class Validation {

    private int length;
    private boolean specialCharacters;
    private boolean upperCases;
    private boolean numbers;
    private int maxAttemptsCount;
    private int changeCycle;
  }
}
