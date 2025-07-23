package io.github.spring.api_parking_manager.model.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import io.github.spring.api_parking_manager.model.EnterpriseModel;
import io.github.spring.api_parking_manager.model.dtos.EnterpriseRequestDTO;
import io.github.spring.api_parking_manager.model.dtos.EnterpriseResponseDTO;

@Component
@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface EnterpriseMapper {

  EnterpriseModel toEntity(EnterpriseRequestDTO enterpriseRequestDTO);

  EnterpriseResponseDTO toResponseDTO(EnterpriseModel enterpriseModel);
}
