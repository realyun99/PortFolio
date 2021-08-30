package com.example.portfolio.util;

import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class PasswordUtil {

  public static boolean isValidPassword(String password) {
    int length = ApplicationContextProvider
        .getEnvironmentProperty("validation.length", Integer.class, 0);
    boolean specialCharacters = ApplicationContextProvider
        .getEnvironmentProperty("validation.special-characters", Boolean.class,
            false);
    boolean upperCases = ApplicationContextProvider
        .getEnvironmentProperty("validation.upper-cases", Boolean.class, false);
    boolean numbers = ApplicationContextProvider
        .getEnvironmentProperty("validation.numbers", Boolean.class, false);

    String baseRegExp = "";
    String specialRegExp = "";
    String upperRegExp = "";
    String numberRegExp = "";

    if (specialCharacters) {
      specialRegExp = LoginAuth.PASSWORD_SPECIAL_REGEXP;
    }
    if (upperCases) {
      upperRegExp = LoginAuth.PASSWORD_UPPER_REGEXP;
    }
    if (numbers) {
      numberRegExp = LoginAuth.PASSWORD_NUMBER_REGEXP;
    }

    baseRegExp =
        "^" + specialRegExp + upperRegExp + numberRegExp
            + "[A-Za-z\\d!@#$%^&*()\\-_=+\\\\\\|\\[\\]{};:\\'\",.<>\\/?]{" + length + ",}";

    if (!StringUtils.isBlank(password) && password.matches(baseRegExp)) {
      return true;
    }
    return false;
  }

  public static String getPasswordRuleMessage() {
    String message = "비밀번호는 ";
    List<String> ruleIncluded = new ArrayList<>();

    int length = ApplicationContextProvider
        .getEnvironmentProperty("validation.length", Integer.class, 0);
    boolean specialCharacters = ApplicationContextProvider
        .getEnvironmentProperty("validation.special-characters", Boolean.class,
            false);
    boolean upperCases = ApplicationContextProvider
        .getEnvironmentProperty("validation.upper-cases", Boolean.class, false);
    boolean numbers = ApplicationContextProvider
        .getEnvironmentProperty("validation.numbers", Boolean.class, false);

    ruleIncluded.add("영문 소문자");

    if (specialCharacters) {
      ruleIncluded.add("특수문자");
    }

    if (upperCases) {
      ruleIncluded.add("영문 대문자");
    }

    if (numbers) {
      ruleIncluded.add("숫자");
    }

    if (!ruleIncluded.isEmpty()) {
      message += String.format("%s를 포함하여 ", StringUtils.join(ruleIncluded, ", "));
    }

    message += String.format("%d자리 이상으로 구성되어야 합니다.", length);

    return message;
  }
}
