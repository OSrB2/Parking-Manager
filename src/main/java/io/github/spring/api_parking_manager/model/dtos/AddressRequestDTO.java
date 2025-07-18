package io.github.spring.api_parking_manager.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequestDTO(
  @NotBlank(message = "Is required!")
  @Size(min = 1, max = 80, message = "Must have between 1 and 80 characters!")
  String street,
  @NotBlank(message = "Is required!")
  @Size(min = 3, max = 80, message = "Must have between 3 and 80 characters!")
  String city,
  @NotBlank(message = "Is required!")
  @Size(min = 2, max = 50, message = "Must have between 2 and 50 characters!")
  String state,
  @NotBlank(message = "Is required!")
  @Size(min = 9, max = 9, message = "Must have format 'xxxxx-xxx'")
  String postalCode
) {
}