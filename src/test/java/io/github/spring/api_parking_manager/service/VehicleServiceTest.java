package io.github.spring.api_parking_manager.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.spring.api_parking_manager.exception.EntityNotFoundException;
import io.github.spring.api_parking_manager.model.Vehicle;
import io.github.spring.api_parking_manager.model.VehicleModel;
import io.github.spring.api_parking_manager.model.dtos.VehicleResponseDTO;
import io.github.spring.api_parking_manager.model.mappers.VehicleMapper;
import io.github.spring.api_parking_manager.repository.VehicleRepository;
import io.github.spring.api_parking_manager.validator.VehicleValidator;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {
  @Mock
  private VehicleRepository vehicleRepository;

  @Mock
  private VehicleMapper vehicleMapper;

  @Mock
  private VehicleValidator validator;

  @InjectMocks
  private VehicleService vehicleService;

  private VehicleModel savedVehicle;
  private VehicleModel mockVehicle;
  private VehicleModel updatedVehicle;
  private VehicleResponseDTO mockResponseDTO;

  @BeforeEach
  void config() {
    mockVehicle = new VehicleModel();
    mockVehicle.setBrand("Honda");
    mockVehicle.setModel("Civic");
    mockVehicle.setColor("Preto");
    mockVehicle.setPlate("ABC1234");
    mockVehicle.setType(Vehicle.CAR);

    LocalDateTime now = LocalDateTime.now();

    mockResponseDTO = new VehicleResponseDTO(
        "Honda",
        "Civic",
        "Preto",
        "ABC1234",
        Vehicle.CAR,
        now,  // createdAt
        now   // updatedAt
    );        

    savedVehicle = new VehicleModel();
    savedVehicle.setBrand("Honda");
    savedVehicle.setModel("Civic");
    savedVehicle.setColor("Preto");
    savedVehicle.setPlate("ABC1234");
    savedVehicle.setType(Vehicle.CAR);

    updatedVehicle = new VehicleModel();
    updatedVehicle.setBrand("Honda");
    updatedVehicle.setModel("Civic");
    updatedVehicle.setColor("Vermelho");
    updatedVehicle.setPlate("ABC1234");
    updatedVehicle.setType(Vehicle.CAR);
  }

  @Test
  @DisplayName("Should register a new vehicle")
  public void testRegisterNewVehicle() {
    doNothing().when(validator).validate(mockVehicle);
    when(vehicleRepository.save(any(VehicleModel.class))).thenReturn(mockVehicle);
    when(vehicleMapper.toResponseDTO(mockVehicle)).thenReturn(mockResponseDTO);

    VehicleResponseDTO result = vehicleService.register(mockVehicle);

    assertNotNull(result);
    assertEquals(mockResponseDTO, result);
    verify(validator).validate(mockVehicle);
    verify(vehicleRepository).save(mockVehicle);
    verify(vehicleMapper).toResponseDTO(mockVehicle);
  }

  @Test
  @DisplayName("Should return a list of vehicles")
  public void testListAllVehicles() {
    List<VehicleModel> vehiclesList = Arrays.asList(mockVehicle, savedVehicle);
    when(vehicleRepository.findAll()).thenReturn(vehiclesList);

    List<VehicleResponseDTO> vehicleResponseDTOList = vehicleService.listAllVehicles();

    assertEquals(2, vehicleResponseDTOList.size());
  }

  @Test
  @DisplayName("Should throw exception if there are no vehicles")
  public void testNoHasVehicles() {
    when(vehicleRepository.findAll()).thenReturn(new ArrayList<>());

    assertThrows(EntityNotFoundException.class, () -> vehicleService.listAllVehicles());
  }

  @Test
  @DisplayName("Should return a vehicle by ID")
  public void testFindVehicleByID() {
    UUID id = UUID.fromString("ad579016-a238-425f-bca7-b59571b5010a");
    mockVehicle.setId(id);

    when(vehicleRepository.findById(id)).thenReturn(Optional.of(mockVehicle));
    when(vehicleMapper.toResponseDTO(mockVehicle)).thenReturn(mockResponseDTO);

    Optional<VehicleResponseDTO> result = vehicleService.findVehicleById(id);

    assertTrue(result.isPresent());
    assertEquals(mockResponseDTO, result.get());
  }

  @Test
  @DisplayName("Should return exception when id not found")
  public void testIdNotFound() throws Exception {
    UUID id = UUID.fromString("ad579016-a238-425f-bca7-b59571b5010a");

    when(vehicleRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> {
      vehicleService.findVehicleById(id);
    });
  }

  @Test
  @DisplayName("Should return a vehicle by Plate")
  public void testFindVehicleByPlate() {
    when(vehicleRepository.findVehicleByPlate(mockVehicle.getPlate())).thenReturn(Optional.of(mockVehicle));
    when(vehicleMapper.toResponseDTO(mockVehicle)).thenReturn(mockResponseDTO);

    Optional<VehicleResponseDTO> result = vehicleService.findVehicleByPlate(mockVehicle.getPlate());

    assertTrue(result.isPresent());
    assertEquals(mockResponseDTO, result.get());
  }

  @Test
  @DisplayName("Should return exception when plate not found")
  public void testPlateNotFound() throws Exception {
    when(vehicleRepository.findVehicleByPlate(mockVehicle.getPlate())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> {
      vehicleService.findVehicleByPlate(mockVehicle.getPlate());
    });
  }

  @Test
  @DisplayName("Should update vehicle by ID")
  public void testUpdateVehicleById() {
    UUID id = UUID.fromString("ad579016-a238-425f-bca7-b59571b5010a");

    mockVehicle.setId(id);
    updatedVehicle.setId(id);

    VehicleResponseDTO updatedResponseDTO = new VehicleResponseDTO(
        updatedVehicle.getBrand(),
        updatedVehicle.getModel(),
        updatedVehicle.getColor(),
        updatedVehicle.getPlate(),
        updatedVehicle.getType(),
        LocalDateTime.now(),
        LocalDateTime.now()
    );

    when(vehicleRepository.findById(id)).thenReturn(Optional.of(mockVehicle));
    when(vehicleRepository.save(any(VehicleModel.class))).thenReturn(mockVehicle);
    when(vehicleMapper.toResponseDTO(mockVehicle)).thenReturn(updatedResponseDTO);

    VehicleResponseDTO result = vehicleService.updateVehicleById(updatedVehicle);

    assertEquals("Vermelho", result.color());
    assertEquals(mockVehicle.getId(), id);

    verify(vehicleRepository).findById(id);
    verify(vehicleRepository).save(mockVehicle);
    verify(vehicleMapper).toResponseDTO(mockVehicle);
  }

  @Test
  @DisplayName("Should delete vehicle by ID")
  public void testDeleteVehicleById() {
    Optional<VehicleModel> vehicleExist = Optional.of(mockVehicle);

    when(vehicleRepository.findById(mockVehicle.getId())).thenReturn(vehicleExist);

    vehicleService.deleteVehicleByID(mockVehicle.getId());
  }
}
