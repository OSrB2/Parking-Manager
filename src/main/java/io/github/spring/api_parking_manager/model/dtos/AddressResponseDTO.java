package io.github.spring.api_parking_manager.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AddressResponse")
public record AddressResponseDTO(
  String street,
  String city,
  String state,
  String postalCode) {}