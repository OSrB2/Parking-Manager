package io.github.spring.api_parking_manager.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.spring.api_parking_manager.model.EnterpriseModel;

@Repository
public interface EnterpriseRepository extends JpaRepository<EnterpriseModel, UUID> {
  
  Optional<EnterpriseModel> findByCnpj(String cnpj);
}
