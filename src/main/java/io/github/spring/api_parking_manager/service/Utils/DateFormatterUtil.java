package io.github.spring.api_parking_manager.service.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatterUtil {
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

  public static String format(LocalDateTime dateTime) {
    if (dateTime == null) return null;
    return dateTime.format(DATE_TIME_FORMATTER);
  }
}
