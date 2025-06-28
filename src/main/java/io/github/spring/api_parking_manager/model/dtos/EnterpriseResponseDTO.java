package io.github.spring.api_parking_manager.model.dtos;

public record EnterpriseResponseDTO(String name,
  String cnpj,
  AddressResponseDTO address,
  Integer motorcycleSpaces,
  Integer carSpaces
  ) {}
