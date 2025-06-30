package io.github.spring.api_parking_manager.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.spring.api_parking_manager.model.EnterpriseModel;
import io.github.spring.api_parking_manager.model.dtos.EnterpriseResponseDTO;

public interface EnterpriseRepository extends JpaRepository<EnterpriseModel, UUID> {
  
  Optional<EnterpriseResponseDTO> findByCnpj(String cnpj);
}
