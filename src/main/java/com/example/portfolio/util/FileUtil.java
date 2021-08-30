package com.example.portfolio.util;

import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FileUtil {

  /**
   * 중복된 파일명이 생성되는 경우를 방지하기 위해 랜덤 파일명 생성
   */
  public static String getRandomFilename(String extension) {
    return String.format("%s.%s", UUID.randomUUID(), extension);
  }
}