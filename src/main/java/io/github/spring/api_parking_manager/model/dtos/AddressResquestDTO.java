package io.github.spring.api_parking_manager.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressResquestDTO(
  @NotBlank(message = "Is required!")
  @Size(min = 1, max = 80, message = "Must have between 1 and 80 characters!")
  String streetAddress,
  @NotBlank(message = "Is required!")
  @Size(min = 3, max = 80, message = "Must have between 3 and 80 characters!")
  String city,
  @NotBlank(message = "Is required!")
  @Size(min = 3, max = 50, message = "Must have between 3 and 50 characters!")
  String State,
  @NotBlank(message = "Is required!")
  @Size(min = 8, max = 8, message = "Must have 7 numbers separated by an ifen 'xxxxx-xxx")
  String postalCode
) {
}