package io.github.spring.api_parking_manager.model.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import io.github.spring.api_parking_manager.model.MovementsModel;
import io.github.spring.api_parking_manager.model.dtos.MovementsRequestDTO;
import io.github.spring.api_parking_manager.model.dtos.MovementsResponseDTO;

@Component
@Mapper(componentModel = "spring")
public interface MovementsMapper {
  
  MovementsModel toEntity(MovementsRequestDTO movementsRequestDTO);

  MovementsResponseDTO toResponseDTO(MovementsModel movementsModel);
}
