package com.example.portfolio.filter;

import com.example.portfolio.util.DateUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseFilter extends QueryStringSupport {

  @DateTimeFormat(pattern = DateUtil.PATTERN_YMD)
  protected LocalDate fStartDate;

  @DateTimeFormat(pattern = DateUtil.PATTERN_YMD)
  protected LocalDate fEndDate = LocalDate.now();

  public LocalDateTime getFStartDateTime() {
    if (fStartDate != null) {
      return fStartDate.atStartOfDay();
    }
    return null;
  }

  public LocalDateTime getFEndDateTime() {
    if (fEndDate != null) {
      return fEndDate.atTime(LocalTime.MAX);
    }
    return null;
  }
}
