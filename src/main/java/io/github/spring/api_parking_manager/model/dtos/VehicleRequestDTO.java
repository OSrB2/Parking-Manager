package io.github.spring.api_parking_manager.model.dtos;

import io.github.spring.api_parking_manager.model.Vehicle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VehicleRequestDTO(
  @NotBlank(message = "Is required!")
  @Size(min = 3, max = 50, message = "Must have between 3 and 50 characters!")
  String brand,
  @NotBlank(message = "Is required!")
  @Size(min = 2, max = 50, message = "Must have between 3 and 50 characters!")
  String model,
  @NotBlank(message = "Is required!")
  @Size(min = 3, max = 25, message = "Must have between 3 and 25 characters!")
  String color,
  @NotBlank(message = "Is required!")
  @Size(min = 7, max = 7, message =  "Must have exaclty 7 characters!")
  String plate,
  @NotNull(message = "Is required!")
  Vehicle type
) {}
