package io.github.spring.api_parking_manager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import io.github.spring.api_parking_manager.exception.EntityNotFoundException;
import io.github.spring.api_parking_manager.model.VehicleModel;
import io.github.spring.api_parking_manager.model.dtos.VehicleResponseDTO;
import io.github.spring.api_parking_manager.model.mappers.VehicleMapper;
import io.github.spring.api_parking_manager.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {
  
  private final VehicleRepository vehicleRepository;
  private final VehicleMapper vehicleMapper;

  public VehicleResponseDTO register(VehicleModel vehicle) {
    VehicleModel savedVehicle = vehicleRepository.save(vehicle);
    return vehicleMapper.toResponseDTO(savedVehicle);
  }

  public List<VehicleResponseDTO> listAllVehicles() {
    List<VehicleModel> vehicles = vehicleRepository.findAll();

    if (vehicles.isEmpty()) {
      throw new EntityNotFoundException("Vehicles not found!");
    }

    List<VehicleResponseDTO> vehicleDTO = new ArrayList<>();

    for (VehicleModel vehicle : vehicles) {
      vehicleDTO.add(vehicleMapper.toResponseDTO(vehicle));
    }

    return vehicleDTO;
  }

  public Optional<VehicleResponseDTO> finalVehicleById(UUID id) {
    Optional<VehicleModel> vehicleOptional = vehicleRepository.findById(id);

    if (vehicleOptional.isEmpty()) {
      throw new EntityNotFoundException("Vehicle not found!");
    }

    return vehicleRepository.findById(id)
      .map(vehicleMapper::toResponseDTO);
  }

  public Optional<VehicleResponseDTO> findVehicleByPlate(String plate) {
    Optional<VehicleModel> vehicleOptional = vehicleRepository.findVehicleByPlate(plate);

    if (vehicleOptional.isEmpty()) {
      throw new EntityNotFoundException("Vehicle not found!");
    }

    return vehicleRepository.findVehicleByPlate(plate)
      .map(vehicleMapper::toResponseDTO);
  }

  public VehicleResponseDTO updateVehicleById(VehicleModel vehicle) {
    VehicleModel vehicleToUpdate = vehicleRepository.findById(vehicle.getId())
      .orElseThrow(() -> new EntityNotFoundException("Vehicle not found!"));

    Optional.ofNullable(vehicle.getBrand()).ifPresent(vehicleToUpdate::setBrand);
    Optional.ofNullable(vehicle.getModel()).ifPresent(vehicleToUpdate::setModel);
    Optional.ofNullable(vehicle.getColor()).ifPresent(vehicleToUpdate::setColor);
    Optional.ofNullable(vehicle.getPlate()).ifPresent(vehicleToUpdate::setPlate);
    Optional.ofNullable(vehicle.getType()).ifPresent(vehicleToUpdate::setType);

    vehicleRepository.save(vehicleToUpdate);
    return vehicleMapper.toResponseDTO(vehicleToUpdate);
  }

  public void deleteVehicleByID(UUID id) {
    VehicleModel vehicleToDelete = vehicleRepository.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("Vehicle not found!"));

      vehicleRepository.delete(vehicleToDelete);
  }
}
