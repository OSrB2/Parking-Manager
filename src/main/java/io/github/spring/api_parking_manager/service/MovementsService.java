package io.github.spring.api_parking_manager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import io.github.spring.api_parking_manager.model.EnterpriseModel;
import io.github.spring.api_parking_manager.model.MovementsModel;
import io.github.spring.api_parking_manager.model.Status;
import io.github.spring.api_parking_manager.model.VehicleModel;
import io.github.spring.api_parking_manager.repository.EnterpriseRepository;
import io.github.spring.api_parking_manager.repository.MovementsRepository;
import io.github.spring.api_parking_manager.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MovementsService {
  
  private final MovementsRepository movementsRepository;
  private final EnterpriseRepository enterpriseRepository;
  private final VehicleRepository vehicleRepository;

  public MovementsModel registerEntry(UUID vehicleId, UUID enterpriseId) {
    VehicleModel vehicle = vehicleRepository.findById(vehicleId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found!"));
    
    EnterpriseModel enterprise = enterpriseRepository.findById(enterpriseId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking not found!"));

    MovementsModel movement = new MovementsModel();

    switch (vehicle.getType()) {
      case CAR -> {
        if (enterprise.getCarSpaces() <= 0) {
        throw new RuntimeException("No spots available!");
        } 
        enterprise.setCarSpaces(enterprise.getCarSpaces() - 1);
        break;
      }
      case MOTORCYCLE -> {
        if (enterprise.getMotorcycleSpaces() <= 0) {
        throw new RuntimeException("No spots available!");
        }
        enterprise.setMotorcycleSpaces(enterprise.getMotorcycleSpaces() - 1);
        break;
      }
      default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported vehicle type!");
    }      

    movement.setStatus(Status.ACTIVE);
    movement.setEntryTime(LocalDateTime.now());
    movement.setVehicle(vehicle);
    movement.setEnterprise(enterprise);
    movement.setType(vehicle.getType());

    enterpriseRepository.save(enterprise);
    return movementsRepository.save(movement);
  }

  public List<MovementsModel> listAllMovements() {
    return movementsRepository.findAll();
  }

  public Optional<MovementsModel> findMovementById(UUID id) {
    return movementsRepository.findById(id);
  }

  public MovementsModel registerExit(UUID id) {
    MovementsModel movement = movementsRepository.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movement not found!"));

    switch (movement.getType()) {
      case CAR:
        movement.getEnterprise().setCarSpaces(movement.getEnterprise().getCarSpaces() + 1);
        break;
      case MOTORCYCLE:
        movement.getEnterprise().setMotorcycleSpaces(movement.getEnterprise().getMotorcycleSpaces() + 1);
        break;
      default:
        break;
    }

    movement.setStatus(Status.FINISHED);
    movement.setDepartureTime(LocalDateTime.now());
    
    return movementsRepository.save(movement);
  }
}
