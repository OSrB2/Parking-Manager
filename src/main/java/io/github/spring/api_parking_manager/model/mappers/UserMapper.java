package io.github.spring.api_parking_manager.model.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import io.github.spring.api_parking_manager.model.UserModel;
import io.github.spring.api_parking_manager.model.dtos.UserRequestDTO;
import io.github.spring.api_parking_manager.model.dtos.UserResponseDTO;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {

  UserModel toEntity(UserRequestDTO UserRequestDTO);

  UserResponseDTO tResponseDTO(UserModel userModel);
}
