package io.github.spring.api_parking_manager.service.Utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class DateTimeUtils {
  public static String calculatePermanenceTime(LocalDateTime entry, LocalDateTime exit) {
    if (exit == null) {
      return "In progress...";
    }

    Duration duration = Duration.between(entry, exit);
    long hours = duration.toHours();
    long minutes = duration.toMinutesPart();
    return String.format("%dh%dm", hours, minutes);
  }
}
