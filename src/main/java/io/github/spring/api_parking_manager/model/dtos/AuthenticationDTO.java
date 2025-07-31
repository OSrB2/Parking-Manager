package io.github.spring.api_parking_manager.model.dtos;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
  @NotBlank(message = "Is required!")
  String login,
  @NotBlank(message = "Is required!")
  String password
) {}
