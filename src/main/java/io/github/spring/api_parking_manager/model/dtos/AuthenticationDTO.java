package io.github.spring.api_parking_manager.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "UserAuthentication")
public record AuthenticationDTO(
  @Schema(
    description = "User email",
    example = "userEmail@example.com",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Is required!")
  String login,

  @Schema(
    description = "User password",
    example = "userpassword",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Is required!")
  String password
) {}
