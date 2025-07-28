package io.github.spring.api_parking_manager.model.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record VehicleReportDTO(
  String plate,
  String type,
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
  LocalDateTime entry,
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
  LocalDateTime exit,
  String lengthStay
) {}
