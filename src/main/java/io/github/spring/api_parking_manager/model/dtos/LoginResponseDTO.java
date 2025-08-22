package io.github.spring.api_parking_manager.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LoginResponse")
public record LoginResponseDTO(
  String token
) {}
