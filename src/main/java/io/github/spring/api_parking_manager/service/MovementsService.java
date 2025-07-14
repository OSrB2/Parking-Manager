package io.github.spring.api_parking_manager.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.spring.api_parking_manager.exception.EntityNotFoundException;
import io.github.spring.api_parking_manager.exception.NoSpotsAvailableException;
import io.github.spring.api_parking_manager.exception.OperationNotPermittedException;
import io.github.spring.api_parking_manager.exception.UnsupportedVehicleTypeException;
import io.github.spring.api_parking_manager.model.EnterpriseModel;
import io.github.spring.api_parking_manager.model.MovementsModel;
import io.github.spring.api_parking_manager.model.Status;
import io.github.spring.api_parking_manager.model.VehicleModel;
import io.github.spring.api_parking_manager.model.dtos.MovementsResponseDTO;
import io.github.spring.api_parking_manager.model.mappers.MovementsMapper;
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
  private final MovementsMapper movementsMapper;

  public MovementsResponseDTO registerEntry(UUID vehicleId, UUID enterpriseId) {
    VehicleModel vehicle = vehicleRepository.findById(vehicleId)
      .orElseThrow(() -> new EntityNotFoundException("Vehicle not found!"));
    
    EnterpriseModel enterprise = enterpriseRepository.findById(enterpriseId)
      .orElseThrow(() -> new EntityNotFoundException("Parking not found!"));

    MovementsModel movement = new MovementsModel();

    switch (vehicle.getType()) {
      case CAR -> {
        if (enterprise.getCarSpaces() <= 0) {
        throw new NoSpotsAvailableException("The car parking spaces are full!");
        } 
        enterprise.setCarSpaces(enterprise.getCarSpaces() - 1);
        break;
      }
      case MOTORCYCLE -> {
        if (enterprise.getMotorcycleSpaces() <= 0) {
        throw new NoSpotsAvailableException("The motorcycle parking spaces are full!");
        }
        enterprise.setMotorcycleSpaces(enterprise.getMotorcycleSpaces() - 1);
        break;
      }
      default -> throw new UnsupportedVehicleTypeException("Unsupported vehicle type!");
    }      

    movement.setStatus(Status.ACTIVE);
    movement.setEntryTime(LocalDateTime.now());
    movement.setVehicle(vehicle);
    movement.setEnterprise(enterprise);
    movement.setType(vehicle.getType());

    enterpriseRepository.save(enterprise);
    movementsRepository.save(movement);
    return movementsMapper.toResponseDTO(movement);
  }

  public List<MovementsResponseDTO> listAllMovements() {
    List<MovementsModel> movements = movementsRepository.findAll();

    if (movements.isEmpty()) {
      throw new EntityNotFoundException("No movements found!");
    }

    List<MovementsResponseDTO> movementsDTO = new ArrayList<>();
    
    for (MovementsModel movement : movements) {
      movementsDTO.add(movementsMapper.toResponseDTO(movement));
    }

    return movementsDTO;
  }

  public List<MovementsResponseDTO> listAllActiveMovements() {
    List<MovementsModel> movements = movementsRepository.findAllByStatusIs(Status.ACTIVE);

    if (movements.isEmpty()) {
      throw new EntityNotFoundException("No active movements found!");
    }

    List<MovementsResponseDTO> movementsDTO = new ArrayList<>();

    for (MovementsModel movement : movements) {
      movementsDTO.add(movementsMapper.toResponseDTO(movement));
    }

    return movementsDTO;
  }

  public List<MovementsResponseDTO> listAllFinishedMovements() {
    List<MovementsModel> movements = movementsRepository.findAllByStatusIs(Status.FINISHED);

    if (movements.isEmpty()) {
      throw new EntityNotFoundException("No finished movements found!");
    }

    List<MovementsResponseDTO> movementsDTO = new ArrayList<>();

    for (MovementsModel movement : movements) {
      movementsDTO.add(movementsMapper.toResponseDTO(movement));
    }

    return movementsDTO;
  }

  public Optional<MovementsModel> findMovementById(UUID id) {
    Optional<MovementsModel> movementsOptional = movementsRepository.findById(id);

    if (movementsOptional.isEmpty()) {
      throw new EntityNotFoundException("Movement not found!");
    }

    return movementsRepository.findById(id);
  }

  public MovementsModel registerExit(UUID id) {
    MovementsModel movement = movementsRepository.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("Movement not found!"));

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

  public void deleteMovementById(UUID id) {
    MovementsModel movementToDelete = movementsRepository.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("Movement not found!"));
    if (movementToDelete.getStatus() == Status.ACTIVE) {
      throw new OperationNotPermittedException("Deletion of active movements is not allowed!");
    }

    movementsRepository.delete(movementToDelete);
  }
}
