package io.github.spring.api_parking_manager.model.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import io.github.spring.api_parking_manager.model.AddressModel;
import io.github.spring.api_parking_manager.model.dtos.AddressResponseDTO;

@Component
@Mapper(componentModel = "spring")
public interface AddressMapper {

  AddressModel toEntity(AddressResponseDTO addressResponseDTO);

  AddressResponseDTO tResponseDTO(AddressModel addressModel);
}
