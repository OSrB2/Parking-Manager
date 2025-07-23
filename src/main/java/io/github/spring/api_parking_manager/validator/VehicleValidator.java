package io.github.spring.api_parking_manager.validator;

import java.util.Optional;

import org.springframework.stereotype.Component;

import io.github.spring.api_parking_manager.exception.DuplicateRecordException;
import io.github.spring.api_parking_manager.model.VehicleModel;
import io.github.spring.api_parking_manager.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VehicleValidator {

  private final VehicleRepository vehicleRepository;

  public void validate(VehicleModel vehicle) {
    if (plateExists(vehicle)) {
      throw new DuplicateRecordException("This PLATE already exists!");
    }
  }

  private boolean plateExists(VehicleModel vehicle) {
    Optional<VehicleModel> byPlate = vehicleRepository.findVehicleByPlate(vehicle.getPlate());

    if (vehicle.getId() == null) {
      return byPlate.isPresent();
    }
    return byPlate
      .map(VehicleModel::getId)
      .stream()
      .anyMatch(id -> !id.equals(vehicle.getId()));
  }
}
