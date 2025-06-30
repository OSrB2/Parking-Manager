package io.github.spring.api_parking_manager.model.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import io.github.spring.api_parking_manager.model.EnterpriseModel;
import io.github.spring.api_parking_manager.model.dtos.EnterpriseResponseDTO;

@Component
@Mapper(componentModel = "spring")
public interface EnterpriseMapper {

  EnterpriseModel toEntity(EnterpriseResponseDTO enterpriseResponseDTO);

  EnterpriseResponseDTO toResponseDTO(EnterpriseModel enterpriseModel);
}
