package io.github.spring.api_parking_manager.model.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.github.spring.api_parking_manager.model.Vehicle;

public record VehicleResponseDTO(
  String brand,
  String model,
  String color,
  String plate,
  Vehicle type,
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
  LocalDateTime createdAt,
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
  LocalDateTime updatedAt
) {} 