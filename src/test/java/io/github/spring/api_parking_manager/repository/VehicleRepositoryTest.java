package io.github.spring.api_parking_manager.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import io.github.spring.api_parking_manager.model.Vehicle;
import io.github.spring.api_parking_manager.model.VehicleModel;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
public class VehicleRepositoryTest {
  
  @Autowired
  private TestEntityManager testEntityManager;
  @Autowired
  private VehicleRepository vehicleRepository;

  private VehicleModel mockVehicle;

  @BeforeEach
  void config() {
    mockVehicle = new VehicleModel();
    mockVehicle.setBrand("Honda");
    mockVehicle.setModel("Civic");
    mockVehicle.setColor("Preto");
    mockVehicle.setPlate("ABC1234");
    mockVehicle.setType(Vehicle.CAR);
  }


  @Test
  @DisplayName("Should find vehicles by plate")
  public void testFindVehicleByPlate() {
    mockVehicle = testEntityManager.persistAndFlush(mockVehicle);

    Optional<VehicleModel> vehicleFinded = vehicleRepository.findVehicleByPlate(mockVehicle.getPlate());

    assertTrue(vehicleFinded.isPresent(), "Vehicle should be present");

    VehicleModel foundVehicle = vehicleFinded.get();
    assertEquals(mockVehicle.getId(), foundVehicle.getId());
    assertEquals(mockVehicle.getBrand(), foundVehicle.getBrand());
    assertEquals(mockVehicle.getModel(), foundVehicle.getModel());
    assertEquals(mockVehicle.getColor(), foundVehicle.getColor());
    assertEquals(mockVehicle.getPlate(), foundVehicle.getPlate());
    assertEquals(mockVehicle.getType(), foundVehicle.getType());
  }
}
