package io.github.spring.api_parking_manager.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.spring.api_parking_manager.model.MovementsModel;

public interface MovementsRepository extends JpaRepository<MovementsModel, UUID>{
  
}
