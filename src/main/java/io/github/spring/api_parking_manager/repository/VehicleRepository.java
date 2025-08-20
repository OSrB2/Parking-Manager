package io.github.spring.api_parking_manager.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.spring.api_parking_manager.model.VehicleModel;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleModel, UUID>{
  
  Optional<VehicleModel> findVehicleByPlate(String plate);
}
