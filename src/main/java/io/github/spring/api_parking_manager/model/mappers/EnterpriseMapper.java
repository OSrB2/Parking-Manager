package io.github.spring.api_parking_manager.model.mappers;

import org.mapstruct.Mapper;

import io.github.spring.api_parking_manager.model.EnterpriseModel;
import io.github.spring.api_parking_manager.model.dtos.EnterpriseResponseDTO;

@Mapper(componentModel = "spring")
public interface EnterpriseMapper {

  EnterpriseModel toEntity(EnterpriseResponseDTO enterpriseResponseDTO);

  EnterpriseResponseDTO toResponseDTO(EnterpriseModel enterpriseModel);
}
