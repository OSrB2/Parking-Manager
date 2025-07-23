package io.github.spring.api_parking_manager.model.dtos;

import java.time.LocalDateTime;

public record VehicleReportDTO(
  String plate,
  String type,
  LocalDateTime entry,
  LocalDateTime exit,
  String lengthStay
) {}
