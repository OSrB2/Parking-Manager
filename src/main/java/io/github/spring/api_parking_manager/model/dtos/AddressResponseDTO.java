package io.github.spring.api_parking_manager.model.dtos;

public record AddressResponseDTO(
  String street,
  String city,
  String state,
  String postalCode) {}