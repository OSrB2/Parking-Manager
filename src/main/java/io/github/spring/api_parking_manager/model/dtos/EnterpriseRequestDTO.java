package io.github.spring.api_parking_manager.model.dtos;

import org.hibernate.validator.constraints.br.CNPJ;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "Enteprise")
public record EnterpriseRequestDTO(
  @Schema(
    description = "Enterprise name",
    example = "ParkSul",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Is required!")
  @Size(min = 3, max = 50, message = "Must have between 3 and 50 characters!")
  String name,

  @Schema(
    description = "License CNPJ of the enterprise",
    example = "49.421.297/0001-58",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Is required!")
  @CNPJ(message = "The document must be valid!")
  String cnpj,

  @Schema(
    description = "Addres of the enterprise",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotNull(message = "Is required!")
  @Valid
  AddressRequestDTO address,

  @Schema(
    description = "Total positions for motorcycles",
    example = "10",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotNull(message = "Is required!")
  @Min(value = 1, message = "Must have at least 1 sport!")
  @Max(value = 30, message = "Must have at most 30 spots!")
  Integer motorcycleSpaces,
  
  @Schema(
    description = "Total positions for cars",
    example = "25",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotNull(message = "Is required!")
  @Min(value = 1, message = "Must have at least 1 sport!")
  @Max(value = 30, message = "Must have at most 30 spots!")
  Integer carSpaces
) {
}
