package io.github.spring.api_parking_manager.model.mappers;

import org.mapstruct.Mapper;
import io.github.spring.api_parking_manager.model.AddressModel;
import io.github.spring.api_parking_manager.model.dtos.AddressResponseDTO;

@Mapper(componentModel = "spring")
public interface AddressMapper {
  
  AddressModel toEntity(AddressResponseDTO addressResponseDTO);

  AddressResponseDTO tResponseDTO(AddressModel addressModel);
}
