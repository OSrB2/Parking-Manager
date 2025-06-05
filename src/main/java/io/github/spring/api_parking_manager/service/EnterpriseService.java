package io.github.spring.api_parking_manager.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import io.github.spring.api_parking_manager.model.AddressModel;
import io.github.spring.api_parking_manager.model.EnterpriseModel;
import io.github.spring.api_parking_manager.repository.EnterpriseRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnterpriseService {
  
  private final EnterpriseRepository enterpriseRepository;

  public EnterpriseModel register(EnterpriseModel enterprise) {
    return enterpriseRepository.save(enterprise);
  }

  public List<EnterpriseModel> listAllEnterprises() {
    return enterpriseRepository.findAll();
  }

  public Optional<EnterpriseModel> findEntenpriseById(UUID id) {
    return enterpriseRepository.findById(id);
  }

  @Transactional
  public EnterpriseModel updateEnterpriseById(EnterpriseModel enterprise) {
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
      
      return enterpriseRepository.save(enterpriseToUpdate);
  }

  public void deleteEnterpriseById(UUID id) {
    EnterpriseModel enterpriseToDelete = enterpriseRepository.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enterprise not found!"));

      enterpriseRepository.delete(enterpriseToDelete);
  }
}
