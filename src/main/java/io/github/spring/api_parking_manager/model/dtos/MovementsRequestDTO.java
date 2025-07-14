package io.github.spring.api_parking_manager.model.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record MovementsRequestDTO(
  @NotNull(message = "Is required!")
  UUID vehicleId,
  @NotNull(message = "Is required!")
  UUID enterpriseId
) {}
