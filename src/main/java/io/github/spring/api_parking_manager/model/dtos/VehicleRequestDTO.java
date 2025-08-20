package io.github.spring.api_parking_manager.model.dtos;

import io.github.spring.api_parking_manager.model.Vehicle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VehicleRequestDTO(
  @Schema(
    description = "Brand of the vehicle",
    example = "Honda",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Is required!")
  @Size(min = 3, max = 50, message = "Must have between 3 and 50 characters!")
  String brand,
  @Schema(
    description = "Model of the vehicle",
    example = "CG 160",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Is required!")
  @Size(min = 2, max = 50, message = "Must have between 3 and 50 characters!")
  String model,
  @Schema(
    description = "Color of the vehicle",
    example = "Preto",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Is required!")
  @Size(min = 3, max = 25, message = "Must have between 3 and 25 characters!")
  String color,
  @Schema(
    description = "License plate of the vehicle",
    example = "BIC1001",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Is required!")
  @Size(min = 7, max = 7, message =  "Must have exaclty 7 characters!")
  String plate,
  @Schema(
    description = "Type of the vehicle",
    example = "MOTORCYCLE",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotNull(message = "Is required!")
  Vehicle type
) {}
