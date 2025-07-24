package io.github.spring.api_parking_manager.model.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonGetter;

import io.github.spring.api_parking_manager.Utils.DateFormatterUtil;

public record ActiveVehicleDTO(
  String plate,
  String model,
  String brand,
  String type,
  LocalDateTime entry
) {

  public String formattedDate() {
    return "entry" + DateFormatterUtil.format(this.entry);
  }

  @JsonGetter("entry")
  public String getEntryFormmatted() {
    return DateFormatterUtil.format(this.entry);
  }
}
