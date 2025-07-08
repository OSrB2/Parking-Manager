package io.github.spring.api_parking_manager.model.dtos;

import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EnterpriseRequestDTO(
  @NotBlank(message = "Is required!")
  @Size(min = 3, max = 50, message = "Must have between 3 and 50 characters!")
  String name,
  @NotBlank(message = "Is required!")
  @CNPJ(message = "The document must be valid!")
  String cnpj,
  @NotBlank(message = "Is required!")
  AddressResquestDTO address,
  @NotNull(message = "Is required!")
  @Size(min = 1, max = 1000, message = "Must have between 1 and 1000 spots!")
  Integer motorcycleSpaces,
  @NotNull(message = "Is required!")
  @Size(min = 1, max = 1000, message = "Must have between 1 and 1000 spots!")
  Integer carSpaces
) {
}
