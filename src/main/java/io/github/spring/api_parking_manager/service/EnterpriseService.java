package io.github.spring.api_parking_manager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import io.github.spring.api_parking_manager.model.AddressModel;
import io.github.spring.api_parking_manager.model.EnterpriseModel;
import io.github.spring.api_parking_manager.model.dtos.EnterpriseResponseDTO;
import io.github.spring.api_parking_manager.model.mappers.EnterpriseMapper;
import io.github.spring.api_parking_manager.repository.EnterpriseRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnterpriseService {
  
  private final EnterpriseRepository enterpriseRepository;
  private final EnterpriseMapper enterpriseMapper;

  public EnterpriseModel register(EnterpriseModel enterprise) {
    return enterpriseRepository.save(enterprise);
  }

  public List<EnterpriseResponseDTO> listAllEnterprises() {
    List<EnterpriseModel> enterprises = enterpriseRepository.findAll();
    List<EnterpriseResponseDTO> enterpriseDTOs = new ArrayList<>();

    for (EnterpriseModel enterprise : enterprises) {
      enterpriseDTOs.add(enterpriseMapper.toResponseDTO(enterprise));
    }
    return enterpriseDTOs;
  }

  public Optional<EnterpriseResponseDTO> findEntenpriseById(UUID id) {
    return enterpriseRepository.findById(id)
      .map(enterpriseMapper::toResponseDTO);
  }

  public Optional<EnterpriseResponseDTO> findEnterpriseByCnpj(String cnpj) {
    return enterpriseRepository.findByCnpj(cnpj)
      .map(enterpriseMapper::toResponseDTO);
  }

  @Transactional
  public EnterpriseResponseDTO updateEnterpriseById(EnterpriseModel enterprise) {
    EnterpriseModel enterpriseToUpdate = enterpriseRepository.findById(enterprise.getId())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enterprise not found!"));

      Optional.ofNullable(enterprise.getName()).ifPresent(enterpriseToUpdate::setName);

      if (enterprise.getAddress() != null) {
        AddressModel currentAddres = enterpriseToUpdate.getAddress();
        AddressModel newAddress = enterprise.getAddress();

        Optional.ofNullable(newAddress.getStreetAddress()).ifPresent(currentAddres::setStreetAddress);
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
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enterprise not found!"));

      enterpriseRepository.delete(enterpriseToDelete);
  }
}
