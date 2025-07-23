package io.github.spring.api_parking_manager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.spring.api_parking_manager.exception.EntityNotFoundException;
import io.github.spring.api_parking_manager.model.AddressModel;
import io.github.spring.api_parking_manager.model.EnterpriseModel;
import io.github.spring.api_parking_manager.model.MovementsModel;
import io.github.spring.api_parking_manager.model.dtos.EnterpriseResponseDTO;
import io.github.spring.api_parking_manager.model.dtos.OccupationDTO;
import io.github.spring.api_parking_manager.model.dtos.ParkingReportDTO;
import io.github.spring.api_parking_manager.model.dtos.VehicleReportDTO;
import io.github.spring.api_parking_manager.model.mappers.EnterpriseMapper;
import io.github.spring.api_parking_manager.repository.EnterpriseRepository;
import io.github.spring.api_parking_manager.repository.MovementsRepository;
import io.github.spring.api_parking_manager.service.Utils.DateTimeUtils;
import io.github.spring.api_parking_manager.validator.EnterpriseValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnterpriseService {
  
  private final EnterpriseRepository enterpriseRepository;
  private final EnterpriseMapper enterpriseMapper;
  private final EnterpriseValidator validator;
  private final MovementsRepository movementsRepository;

  public EnterpriseResponseDTO register(EnterpriseModel enterprise) {
    validator.validate(enterprise);
    EnterpriseModel savedEnterprise = enterpriseRepository.save(enterprise);
    return enterpriseMapper.toResponseDTO(savedEnterprise);
  }

  public List<EnterpriseResponseDTO> listAllEnterprises() {
    List<EnterpriseModel> enterprises = enterpriseRepository.findAll();

    if (enterprises.isEmpty()) {
      throw new EntityNotFoundException("Enterprises not found!");
    }

    List<EnterpriseResponseDTO> enterpriseDTOs = new ArrayList<>();

    for (EnterpriseModel enterprise : enterprises) {
      enterpriseDTOs.add(enterpriseMapper.toResponseDTO(enterprise));
    }
    return enterpriseDTOs;
  }

  public Optional<EnterpriseResponseDTO> findEntenpriseById(UUID id) {
    Optional<EnterpriseModel> enterpriseOptional = enterpriseRepository.findById(id);

    if (enterpriseOptional.isEmpty()) {
      throw new EntityNotFoundException("Enterprise not found!");
    }

    return enterpriseRepository.findById(id)
      .map(enterpriseMapper::toResponseDTO);
  }

  public Optional<EnterpriseResponseDTO> findEnterpriseByCnpj(String cnpj) {
    Optional<EnterpriseModel> enterpriseOptional = enterpriseRepository.findByCnpj(cnpj);

    if (enterpriseOptional.isEmpty()) {
      throw new EntityNotFoundException("Enterprise not found!");
    }  

    return enterpriseRepository.findByCnpj(cnpj)
      .map(enterpriseMapper::toResponseDTO);
  }

  @Transactional
  public EnterpriseResponseDTO updateEnterpriseById(EnterpriseModel enterprise) {
    EnterpriseModel enterpriseToUpdate = enterpriseRepository.findById(enterprise.getId())
      .orElseThrow(() -> new EntityNotFoundException("Enterprise not found!"));

    Optional.ofNullable(enterprise.getName()).ifPresent(enterpriseToUpdate::setName);

    if (enterprise.getAddress() != null) {
      AddressModel currentAddres = enterpriseToUpdate.getAddress();
      AddressModel newAddress = enterprise.getAddress();

      Optional.ofNullable(newAddress.getStreet()).ifPresent(currentAddres::setStreet);
      Optional.ofNullable(newAddress.getCity()).ifPresent(currentAddres::setCity);
      Optional.ofNullable(newAddress.getState()).ifPresent(currentAddres::setState);
      Optional.ofNullable(newAddress.getPostalCode()).ifPresent(currentAddres::setPostalCode);
    }

    Optional.ofNullable(enterprise.getMotorcycleSpaces()).ifPresent(enterpriseToUpdate::setMotorcycleSpaces);
    Optional.ofNullable(enterprise.getCarSpaces()).ifPresent(enterpriseToUpdate::setCarSpaces);
    
    enterpriseRepository.save(enterpriseToUpdate);
    return enterpriseMapper.toResponseDTO(enterpriseToUpdate);
  }

  public void deleteEnterpriseById(UUID id) {
    EnterpriseModel enterpriseToDelete = enterpriseRepository.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("Enterprise not found!"));

    enterpriseRepository.delete(enterpriseToDelete);
  }

  public ParkingReportDTO generateOccupancyReport(UUID enterpriseId) {
    EnterpriseModel enterprise = enterpriseRepository.findById(enterpriseId)
      .orElseThrow(() -> new EntityNotFoundException("Enterprise not found!"));

      long parkedCars = movementsRepository.countParkedCars(enterpriseId);
      long parkedMotorcycles = movementsRepository.countParkedMotorcycles(enterpriseId);

      OccupationDTO cars = new OccupationDTO(enterprise.getCarSpaces(), parkedCars);
      OccupationDTO motorcycles = new OccupationDTO(enterprise.getMotorcycleSpaces(), parkedMotorcycles);

      return new ParkingReportDTO(cars, motorcycles);
  }

  public List<VehicleReportDTO> generateMovementReport(UUID enterpriseId) {
    List<MovementsModel> movement =movementsRepository.findByEnterpriseId(enterpriseId);

    if (movement.isEmpty()) {
      throw new EntityNotFoundException("Enterprise not found!");
    }

    return movement.stream()
      .map(m -> {
        String time = DateTimeUtils.calculatePermanenceTime(m.getEntryTime(), m.getDepartureTime());
        return new VehicleReportDTO(
          m.getVehicle().getPlate(),
          m.getVehicle().getType().toString(),
          m.getEntryTime(),
          m.getDepartureTime(),
          time
        );
      })
      .collect(Collectors.toList());
  }
}
