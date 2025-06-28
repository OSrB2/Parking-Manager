package io.github.spring.api_parking_manager.model.dtos;

public record AddressResponseDTO(
  String streetAddress,
  String city,
  String state,
  String postalCode) {}