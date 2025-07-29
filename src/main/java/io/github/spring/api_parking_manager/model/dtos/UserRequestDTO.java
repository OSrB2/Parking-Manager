package io.github.spring.api_parking_manager.model.dtos;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
  @NotBlank(message = "Is required!")
  @Size(min = 3, max = 50, message = "Must have betwee 3 and 50 characters")
  String name,
  @NotBlank(message = "Is required!")
  @Size(min = 3, max = 50, message = "Must have betwee 3 and 50 characters")
  String lastName,
  @NotBlank(message = "Is required!")
  @CPF(message = "The document must be valid!")
  String cpf,
  @NotBlank(message = "Is required!")
  @Email(message = "The email must be valid!")
  String email,
  @NotBlank(message = "Is required!")
  @Size(min = 6, max = 255, message = "This password is not valid!")
  String password,
  @NotBlank(message = "Is required!")
  String role
) {}
