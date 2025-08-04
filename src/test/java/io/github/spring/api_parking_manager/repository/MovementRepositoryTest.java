package io.github.spring.api_parking_manager.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import io.github.spring.api_parking_manager.model.AddressModel;
import io.github.spring.api_parking_manager.model.EnterpriseModel;
import io.github.spring.api_parking_manager.model.MovementsModel;
import io.github.spring.api_parking_manager.model.Status;
import io.github.spring.api_parking_manager.model.Vehicle;
import io.github.spring.api_parking_manager.model.VehicleModel;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
public class MovementRepositoryTest {
  
  @Autowired
  private TestEntityManager testEntityManager;

  @Autowired
  private MovementsRepository movementsRepository;

  private MovementsModel mockCarMovements;
  private MovementsModel mockMotorcycleMovements;
  private VehicleModel mockCarVehicle;
  private VehicleModel mockMotorcycleVehicle;
  private EnterpriseModel mockEnterprise;
  private AddressModel mockAddress;
  
  @BeforeEach
  void config() {
    mockCarVehicle = new VehicleModel();
    mockCarVehicle.setBrand("Honda");
    mockCarVehicle.setModel("Civic");
    mockCarVehicle.setColor("Preto");
    mockCarVehicle.setPlate("ABC1234");
    mockCarVehicle.setType(Vehicle.CAR);

    mockMotorcycleVehicle = new VehicleModel();
    mockMotorcycleVehicle.setBrand("Yamaha");
    mockMotorcycleVehicle.setModel("Fazer 250");
    mockMotorcycleVehicle.setColor("Vermelho");
    mockMotorcycleVehicle.setPlate("MOT2002");
    mockMotorcycleVehicle.setType(Vehicle.MOTORCYCLE);

    mockAddress = new AddressModel();
    mockAddress.setStreet("Avenida Paulista");
    mockAddress.setCity("São Paulo");
    mockAddress.setState("SP");
    mockAddress.setPostalCode("01310-100");

    mockEnterprise = new EnterpriseModel();
    mockEnterprise.setName("Estaciona Fácil");
    mockEnterprise.setCnpj("63.408.531/0001-38");
    mockEnterprise.setAddress(mockAddress);
    mockEnterprise.setMotorcycleSpaces(10);
    mockEnterprise.setCarSpaces(25);

    mockCarMovements = new MovementsModel();
    mockCarMovements.setVehicle(mockCarVehicle);
    mockCarMovements.setEnterprise(mockEnterprise);
    mockCarMovements.setEntryTime(LocalDateTime.now());
    mockCarMovements.setStatus(Status.ACTIVE);
    mockCarMovements.setType(Vehicle.CAR);

    mockMotorcycleMovements = new MovementsModel();
    mockMotorcycleMovements.setVehicle(mockMotorcycleVehicle);
    mockMotorcycleMovements.setEnterprise(mockEnterprise);
    mockMotorcycleMovements.setEntryTime(LocalDateTime.now());
    mockMotorcycleMovements.setStatus(Status.ACTIVE);
    mockMotorcycleMovements.setType(Vehicle.MOTORCYCLE);
  }

  @Test
  @DisplayName("Should count parked cars on enterprise by ID")
  public void testCountParkedCars() {
    testEntityManager.persistAndFlush(mockEnterprise);
    testEntityManager.persistAndFlush(mockCarVehicle);

    mockCarMovements = testEntityManager.persistAndFlush(mockCarMovements);

    UUID enterpriseID = mockCarMovements.getEnterprise().getId();
    
    long parkedCars = movementsRepository.countParkedCars(enterpriseID);

    assertTrue(parkedCars != 0, "At least 1 car parked");
  }

  @Test
  @DisplayName("Should count parkd motorcycles on enterprise by ID")
  public void testCountParkdMotorcycles() {
    testEntityManager.persistAndFlush(mockEnterprise);
    testEntityManager.persistAndFlush(mockMotorcycleVehicle);

    mockMotorcycleMovements = testEntityManager.persistAndFlush(mockMotorcycleMovements);

    UUID enterpriseID = mockMotorcycleMovements.getEnterprise().getId();
    
    long parkedMotorcycles = movementsRepository.countParkedMotorcycles(enterpriseID);

    assertTrue(parkedMotorcycles != 0, "At least 1 motorcycle parked");
  }

  @Test
  @DisplayName("Should find enterprise by ID")
  public void testFindEnterpriseById() {
    testEntityManager.persistAndFlush(mockEnterprise);
    testEntityManager.persistAndFlush(mockCarVehicle);
    testEntityManager.persistAndFlush(mockCarMovements);

    UUID enterpriseID = mockEnterprise.getId();

    List<MovementsModel> movements = movementsRepository.findByEnterpriseId(enterpriseID);

    assertNotNull(movements);
    assertFalse(movements.isEmpty(), "Should return at least one movement for the enterprise");
    assertEquals(enterpriseID, movements.get(0).getEnterprise().getId());
  }

  @Test
  @DisplayName("Should return parked cars (departure_time is NULL) for given enterprise")
  public void testFindParkedCarsByEnterpriseId() {
    testEntityManager.persistAndFlush(mockEnterprise);
    testEntityManager.persistAndFlush(mockCarVehicle);

    mockCarMovements.setDepartureTime(null);
    testEntityManager.persistAndFlush(mockCarMovements);

    UUID enterpriseID = mockEnterprise.getId();

    List<MovementsModel> results = movementsRepository.findByIdCarsParkedOnEnterprise(enterpriseID);

    assertNotNull(results);
    assertFalse(results.isEmpty(), "Should return at least one parked movement");

    for (MovementsModel m : results) {
        assertNull(m.getDepartureTime());
        assertEquals(enterpriseID, m.getEnterprise().getId());
    }
  }
}
