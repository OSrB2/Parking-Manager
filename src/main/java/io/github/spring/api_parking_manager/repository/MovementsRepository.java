package io.github.spring.api_parking_manager.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.spring.api_parking_manager.model.MovementsModel;
import io.github.spring.api_parking_manager.model.Status;

public interface MovementsRepository extends JpaRepository<MovementsModel, UUID>{

  List<MovementsModel> findAllByStatusIs(Status status);
}
