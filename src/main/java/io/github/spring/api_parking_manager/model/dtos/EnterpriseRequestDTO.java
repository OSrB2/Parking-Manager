package io.github.spring.api_parking_manager.model.dtos;

import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
  @NotNull(message = "Is required!")
  @Valid
  AddressResquestDTO address,
  @NotNull(message = "Is required!")
  @Min(value = 1, message = "Must have at least 1 sport!")
  Integer motorcycleSpaces,
  @NotNull(message = "Is required!")
  @Max(value = 30, message = "Must have at most 30 spots!")
  Integer carSpaces
) {
}
