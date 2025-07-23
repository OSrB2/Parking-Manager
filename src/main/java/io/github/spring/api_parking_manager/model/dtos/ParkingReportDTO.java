package io.github.spring.api_parking_manager.model.dtos;

public record ParkingReportDTO(
  OccupationDTO cars,
  OccupationDTO motorcycles
) {}
