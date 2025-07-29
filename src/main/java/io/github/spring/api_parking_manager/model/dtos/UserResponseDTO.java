package io.github.spring.api_parking_manager.model.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record UserResponseDTO(
  String name,
  String lastName,
  String cpf,
  String email,
  String hole,
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
  LocalDateTime createdAt,
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
  LocalDateTime updatedAt
) {} 
