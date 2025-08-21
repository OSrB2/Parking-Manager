package io.github.spring.api_parking_manager.model.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.github.spring.api_parking_manager.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MovementsResponse")
public record MovementsResponseDTO(
  EnterpriseResponseDTO enterprise,
  VehicleResponseDTO vehicle,
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
  LocalDateTime entryTime,
  Status status,
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
  LocalDateTime departureTime
) {
}
