package io.github.spring.api_parking_manager.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.spring.api_parking_manager.model.VehicleModel;

public interface VehicleRepository extends JpaRepository<VehicleModel, UUID>{
  
  Optional<VehicleModel> findVehicleByPlate(String plate);
}
