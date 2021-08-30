package com.example.portfolio.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtil {

  public static final int MINUTES_OF_DAY = 1440;

  public static final String PATTERN_YMD = "yyyy-MM-dd";
  public static final String PATTERN_YM = "yyyy-MM";
  public static final String PATTERN_MD = "M/d";
  public static final String PATTERN_YMDHMS = "yyyy-MM-dd HH:mm:ss";
  public static final String PATTERN_YMDTHMS = "yyyy-MM-dd'T'HH:mm:ss";
  public static final String PATTERN_HMS = "HH:mm:ss";
  public static final String PATTERN_HM = "HH:mm";
  public static final String PATTERN_YMDHM = "yyyy-MM-dd HH:mm";
  public static final String PATTERN_YMDAHM = "yyyy-MM-dd a hh시 mm분";
  public static final String PATTERN_H = "HH시";

  public static final DateTimeFormatter FORMAT_YMDHMS = DateTimeFormatter.ofPattern(PATTERN_YMDHMS);
  public static final DateTimeFormatter FORMAT_YMDHM = DateTimeFormatter.ofPattern(PATTERN_YMDHM);
  public static final DateTimeFormatter FORMAT_MD = DateTimeFormatter.ofPattern(PATTERN_MD);
  public static final DateTimeFormatter FORMAT_YMD = DateTimeFormatter.ofPattern(PATTERN_YMD);
  public static final DateTimeFormatter FORMAT_YM = DateTimeFormatter.ofPattern(PATTERN_YM);
  public static final DateTimeFormatter FORMAT_HM = DateTimeFormatter.ofPattern(PATTERN_HM);
  public static final DateTimeFormatter FORMAT_HMS = DateTimeFormatter.ofPattern(PATTERN_HMS);
  public static final DateTimeFormatter FORMAT_H = DateTimeFormatter.ofPattern(PATTERN_H);

  public static DateTimeFormatter getDateTimeFormatter(String pattern) {
    return DateTimeFormatter.ofPattern(pattern);
  }

  public static Date fromLocalDate(LocalDate ld) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.PATTERN_YMD);
    return sdf.parse(ld.format(DateUtil.FORMAT_YMD));
  }

  public static boolean isBetween(LocalDate start, LocalDate end, LocalDate target) {
    return target.isAfter(start) && target.isBefore(end);
  }

  public static boolean isBetween(LocalTime start, LocalTime end, LocalTime target) {
    return target.isAfter(start) && target.isBefore(end);
  }

  public static boolean isBetween(LocalDateTime start, LocalDateTime end, LocalDateTime target) {
    return target.isAfter(start) && target.isBefore(end);
  }
}
