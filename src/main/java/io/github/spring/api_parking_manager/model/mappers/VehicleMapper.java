package io.github.spring.api_parking_manager.model.mappers;

import org.mapstruct.Mapper;

import io.github.spring.api_parking_manager.model.VehicleModel;
import io.github.spring.api_parking_manager.model.dtos.VehicleResponseDTO;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

  VehicleModel toEntity(VehicleResponseDTO vehicleResponseDTO);

  VehicleResponseDTO toResponseDTO(VehicleModel vehicleModel);
} 
