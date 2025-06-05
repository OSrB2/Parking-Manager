package io.github.spring.api_parking_manager.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.github.spring.api_parking_manager.model.VehicleModel;
import io.github.spring.api_parking_manager.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {
  
  private final VehicleRepository vehicleRepository;

  public VehicleModel register(VehicleModel vehicle) {
    return vehicleRepository.save(vehicle);
  }

  public List<VehicleModel> listAllVehicles() {
    return vehicleRepository.findAll();
  }

  public Optional<VehicleModel> finalVehicleById(UUID id) {
    return vehicleRepository.findById(id);
  }

  public VehicleModel updateVehicleById(VehicleModel vehicle) {
    VehicleModel vehicleToUpdate = vehicleRepository.findById(vehicle.getId())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found!"));

    Optional.ofNullable(vehicle.getBrand()).ifPresent(vehicleToUpdate::setBrand);
    Optional.ofNullable(vehicle.getModel()).ifPresent(vehicleToUpdate::setModel);
    Optional.ofNullable(vehicle.getColor()).ifPresent(vehicleToUpdate::setColor);
    Optional.ofNullable(vehicle.getPlate()).ifPresent(vehicleToUpdate::setPlate);
    Optional.ofNullable(vehicle.getType()).ifPresent(vehicleToUpdate::setType);

    return vehicleRepository.save(vehicleToUpdate);
  }

  public void deleteVehicleByID(UUID id) {
    VehicleModel vehicleToDelete = vehicleRepository.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vechicle not found!"));

      vehicleRepository.delete(vehicleToDelete);
  }
}
