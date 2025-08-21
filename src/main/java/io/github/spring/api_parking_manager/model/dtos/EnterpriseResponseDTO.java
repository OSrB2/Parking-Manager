package io.github.spring.api_parking_manager.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "EnterpriseResponse")
public record EnterpriseResponseDTO(
  String name,
  String cnpj,
  AddressResponseDTO address,
  Integer motorcycleSpaces,
  Integer carSpaces
  ) {}
