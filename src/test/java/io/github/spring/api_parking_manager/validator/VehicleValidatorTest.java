package io.github.spring.api_parking_manager.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.github.spring.api_parking_manager.exception.DuplicateRecordException;
import io.github.spring.api_parking_manager.model.VehicleModel;
import io.github.spring.api_parking_manager.repository.VehicleRepository;

public class VehicleValidatorTest {
  
  @Mock
  private VehicleRepository vehicleRepository;

  private VehicleValidator validator;

  @BeforeEach
  void config() {
    MockitoAnnotations.openMocks(this);
    validator = new VehicleValidator(vehicleRepository);
  }

  @Test
  @DisplayName("Should throw exception when plate already exists for a new vehicle")
  public void testThrowExceptionWhenPlateAlreadyExists() {
    UUID id = UUID.fromString("7104bb59-2e2e-451e-abaa-a9c63910a1bf");

    VehicleModel vehicle = new VehicleModel();
    vehicle.setId(null);
    vehicle.setPlate("LMT1356");

    VehicleModel existingVehicle = new VehicleModel();
    existingVehicle.setId(id);
    existingVehicle.setPlate("LMT1356");

    when(vehicleRepository.findVehicleByPlate("LMT1356")).thenReturn(Optional.of(existingVehicle));

    DuplicateRecordException except = assertThrows(DuplicateRecordException.class,
                                      () -> validator.validate(vehicle));

    assertEquals("This PLATE already exists!", except.getMessage());
    verify(vehicleRepository, times(1)).findVehicleByPlate("LMT1356");
  }

  @Test
  @DisplayName("Should not throw an exception when Plate does not exist")
  public void testNotThrowsExceptionWhenPlateDoesNotExist() {
    VehicleModel vehicle = new VehicleModel();
    vehicle.setId(null);
    vehicle.setPlate("LMT1356");

    when(vehicleRepository.findVehicleByPlate("LMT1356")).thenReturn(Optional.empty());

    assertDoesNotThrow(() -> validator.validate(vehicle));
  }

  @Test
  @DisplayName("Should throw an exception when the Plate belongs to another vehicle")
  public void testThrowExceptionWhenPlateBelongToDifferentVehicle() {
    UUID id = UUID.fromString("7104bb59-2e2e-451e-abaa-a9c63910a1bf");
    UUID id2 = UUID.fromString("601e7093-8c10-40b8-809a-1c992a234b54");

    VehicleModel vehicle = new VehicleModel();
    vehicle.setId(id);
    vehicle.setPlate("LMT1356");

    VehicleModel existingVehicle = new VehicleModel();
    vehicle.setId(id2);
    vehicle.setPlate("LMT1356");

    when(vehicleRepository.findVehicleByPlate("LMT1356")).thenReturn(Optional.of(existingVehicle));

    assertThrows(DuplicateRecordException.class, () -> validator.validate(vehicle));
  }
}
