package io.github.spring.api_parking_manager.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "Address")
public record AddressRequestDTO(
  @Schema(
    description = "Street name",
    example = "Rua da Fiação, 284",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Is required!")
  @Size(min = 1, max = 80, message = "Must have between 1 and 80 characters!")
  String street,
  @Schema(
    description = "City name",
    example = "São Paulo",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Is required!")
  @Size(min = 3, max = 80, message = "Must have between 3 and 80 characters!")
  String city,
  @Schema(
    description = "State acronym",
    example = "SP",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Is required!")
  @Size(min = 2, max = 50, message = "Must have between 2 and 50 characters!")
  String state,
  @Schema(
    description = "Postal code of the address",
    example = "13049-139",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Is required!")
  @Size(min = 9, max = 9, message = "Must have format 'xxxxx-xxx'")
  String postalCode
) {
}