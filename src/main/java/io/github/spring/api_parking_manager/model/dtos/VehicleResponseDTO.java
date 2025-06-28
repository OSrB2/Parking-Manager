package io.github.spring.api_parking_manager.model.dtos;

import io.github.spring.api_parking_manager.model.Vehicle;

public record VehicleResponseDTO(
  String brand,
  String model,
  String color,
  String plate,
  Vehicle type
) {} 