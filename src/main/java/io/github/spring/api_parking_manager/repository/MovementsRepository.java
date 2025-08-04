package io.github.spring.api_parking_manager.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.spring.api_parking_manager.model.MovementsModel;
import io.github.spring.api_parking_manager.model.Status;

public interface MovementsRepository extends JpaRepository<MovementsModel, UUID>{

  List<MovementsModel> findAllByStatusIs(Status status);

  @Query("SELECT COUNT(m) FROM MovementsModel m WHERE m.departureTime is NULL AND m.vehicle.type = 'CAR' AND m.enterprise.id = :enterpriseId")
  long countParkedCars(@Param("enterpriseId") UUID enterpriseId);

  @Query("SELECT COUNT(m) FROM MovementsModel m WHERE m.departureTime IS NULL AND m.vehicle.type = 'MOTORCYCLE' AND m.enterprise.id = :enterpriseId")
  long countParkedMotorcycles(@Param("enterpriseId") UUID enterpriseId);

  @Query("SELECT m FROM MovementsModel m WHERE m.enterprise.id = :enterpriseId")
  List<MovementsModel> findByEnterpriseId(@Param("enterpriseId") UUID enterpriseId);

  @Query(value = "SELECT * FROM tb_movements WHERE enterprise_id = ?1 AND departure_time IS NULL", nativeQuery = true)
  List<MovementsModel> findByIdCarsParkedOnEnterprise(UUID enterpriseId);
}
