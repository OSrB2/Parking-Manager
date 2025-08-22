package io.github.spring.api_parking_manager.model.dtos;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "Movements")
public record MovementsRequestDTO(
  @Schema(
    description = "Vehicle ID",
    example = "7f6899c2-7979-4048-8ff6-a7d1c75083ef",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotNull(message = "Is required!")
  UUID vehicleId,

  @Schema(
    description = "Enterprise ID",
    example = "5d278461-cb2a-42cd-91ec-de02de142610",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotNull(message = "Is required!")
  UUID enterpriseId
) {}
