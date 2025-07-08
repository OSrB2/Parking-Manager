package io.github.spring.api_parking_manager.model.dtos;

import jakarta.validation.constraints.NotBlank;

public record MovementsRequestDTO(
  @NotBlank(message = "Is required!")
  VehicleRequestDTO vehicle,
  @NotBlank(message = "Is required!")
  EnterpriseRequestDTO enterprise
) {}
