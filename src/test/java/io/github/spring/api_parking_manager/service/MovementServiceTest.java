package io.github.spring.api_parking_manager.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.spring.api_parking_manager.exception.EntityNotFoundException;
import io.github.spring.api_parking_manager.exception.OperationNotPermittedException;
import io.github.spring.api_parking_manager.model.AddressModel;
import io.github.spring.api_parking_manager.model.EnterpriseModel;
import io.github.spring.api_parking_manager.model.MovementsModel;
import io.github.spring.api_parking_manager.model.Status;
import io.github.spring.api_parking_manager.model.Vehicle;
import io.github.spring.api_parking_manager.model.VehicleModel;
import io.github.spring.api_parking_manager.model.dtos.AddressResponseDTO;
import io.github.spring.api_parking_manager.model.dtos.EnterpriseResponseDTO;
import io.github.spring.api_parking_manager.model.dtos.MovementsResponseDTO;
import io.github.spring.api_parking_manager.model.dtos.VehicleResponseDTO;
import io.github.spring.api_parking_manager.model.mappers.MovementsMapper;
import io.github.spring.api_parking_manager.repository.EnterpriseRepository;
import io.github.spring.api_parking_manager.repository.MovementsRepository;
import io.github.spring.api_parking_manager.repository.VehicleRepository;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovementServiceTest {
  @Mock
  private MovementsRepository movementsRepository;

  @Mock
  private VehicleRepository vehicleRepository;

  @Mock
  private EnterpriseRepository enterpriseRepository;

  @Mock
  private MovementsMapper movementsMapper;

  @InjectMocks
  private MovementsService movementsService;

  private MovementsModel mockMovements;
  private MovementsModel savedMovements;
  private AddressModel mockAddress;
  private AddressResponseDTO mockAddressResponseDTO;
  private EnterpriseModel mockEnterprise;
  private EnterpriseResponseDTO mockEnterpriseResponseDTO;
  private VehicleModel mockVehicle;
  private VehicleResponseDTO mockVehicleResponseDTO;
  private MovementsResponseDTO expectedResponse;

  @BeforeEach
  void config() {
    mockVehicle = new VehicleModel();
    mockVehicle.setBrand("Honda");
    mockVehicle.setModel("Civic");
    mockVehicle.setColor("Preto");
    mockVehicle.setPlate("ABC1234");
    mockVehicle.setType(Vehicle.CAR);

    mockVehicleResponseDTO = new VehicleResponseDTO(
    mockVehicle.getBrand(),
    mockVehicle.getModel(),
    mockVehicle.getColor(),
    mockVehicle.getPlate(),
    mockVehicle.getType(),
    mockVehicle.getCreatedAt(),
    mockVehicle.getUpdatedAt());

    mockAddress = new AddressModel();
    mockAddress.setStreet("Avenida Paulista");
    mockAddress.setCity("São Paulo");
    mockAddress.setState("SP");
    mockAddress.setPostalCode("01310-100");

    mockAddressResponseDTO = new AddressResponseDTO(
    mockAddress.getStreet(),
    mockAddress.getCity(),
    mockAddress.getState(),
    mockAddress.getPostalCode());

    mockEnterprise = new EnterpriseModel();
    mockEnterprise.setName("Estaciona Fácil");
    mockEnterprise.setCnpj("63.408.531/0001-38");
    mockEnterprise.setAddress(mockAddress);
    mockEnterprise.setCarSpaces(5);
    mockEnterprise.setMotorcycleSpaces(10);

    mockEnterpriseResponseDTO = new EnterpriseResponseDTO(
    mockEnterprise.getName(),
    mockEnterprise.getCnpj(),
    mockAddressResponseDTO,
    mockEnterprise.getMotorcycleSpaces(),
    mockEnterprise.getCarSpaces());

    mockMovements = new MovementsModel();
    mockMovements.setEnterprise(mockEnterprise);
    mockMovements.setVehicle(mockVehicle);
    mockMovements.setEntryTime(LocalDateTime.now());
    mockMovements.setDepartureTime(null);
    mockMovements.setStatus(Status.ACTIVE);
    mockMovements.setType(Vehicle.CAR);
    mockMovements.setCreatedAt(LocalDateTime.now());
    mockMovements.setUpdatedAt(LocalDateTime.now());

    savedMovements = new MovementsModel();
    savedMovements.setEnterprise(mockEnterprise);
    savedMovements.setVehicle(mockVehicle);
    savedMovements.setEntryTime(LocalDateTime.now());
    savedMovements.setDepartureTime(null);
    savedMovements.setStatus(Status.ACTIVE);
    savedMovements.setType(Vehicle.CAR);
    savedMovements.setCreatedAt(LocalDateTime.now());
    savedMovements.setUpdatedAt(LocalDateTime.now());

    expectedResponse = new MovementsResponseDTO(
    mockEnterpriseResponseDTO,
    mockVehicleResponseDTO,
    mockMovements.getEntryTime(), 
    Status.ACTIVE,
    null);
  }

  @Test
  @DisplayName("Should register a new movement")
  public void testRegisterNewMovement() {
    UUID vehicleId = mockVehicle.getId();
    UUID enterpriseId = mockEnterprise.getId();

    when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(mockVehicle));
    when(enterpriseRepository.findById(enterpriseId)).thenReturn(Optional.of(mockEnterprise));
    when(movementsRepository.save(any(MovementsModel.class))).thenReturn(mockMovements);
    when(enterpriseRepository.save(any(EnterpriseModel.class))).thenReturn(mockEnterprise);
    when(movementsMapper.toResponseDTO(any(MovementsModel.class))).thenReturn(expectedResponse);

    MovementsResponseDTO result = movementsService.registerEntry(vehicleId, enterpriseId);

    assertNotNull(result);
    assertEquals(expectedResponse, result);
  }

  @Test
  @DisplayName("Shoud return a list of all movements")
  public void testListAllMovements() {
    List<MovementsModel> movementsList = Arrays.asList(mockMovements, savedMovements);
    when(movementsRepository.findAll()).thenReturn(movementsList);

    List<MovementsResponseDTO> movementsResponseDTOList = movementsService.listAllMovements();

    assertEquals(2, movementsResponseDTOList.size());
  }

  @Test
  @DisplayName("Should throw exception if there are no movements")
  public void testNoHasMovements() {
    when(movementsRepository.findAll()).thenReturn(new ArrayList<>());

    assertThrows(EntityNotFoundException.class, () -> {
      movementsService.listAllMovements();
    });
  }

  @Test
  @DisplayName("Shoud return movement by ID")
  public void testFindeMovementById() {
    UUID id = UUID.fromString("ad579016-a238-425f-bca7-b59571b5010a");
    mockMovements.setId(id);

    when(movementsRepository.findById(id)).thenReturn(Optional.of(mockMovements));
    when(movementsMapper.toResponseDTO(mockMovements)).thenReturn(expectedResponse);

    Optional<MovementsResponseDTO> result = movementsService.findMovementById(id);
    
    assertTrue(result.isPresent());
    assertEquals(expectedResponse, result.get());
  }

  @Test
  @DisplayName("Should return exception when id not found")
  public void testIdNotFound() {
    UUID id = UUID.fromString("ad579016-a238-425f-bca7-b59571b5010a");

    when(movementsRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> {
      movementsService.findMovementById(id);
    });
  }

  @Test
  @DisplayName("Should return a list of all active movements")
  public void testListAllActiveMovements() {
 
    List<MovementsModel> movementsList = Arrays.asList(mockMovements, savedMovements);
    when(movementsRepository.findAllByStatusIs(Status.ACTIVE)).thenReturn(movementsList);

    List<MovementsResponseDTO> result = movementsService.listAllActiveMovements();

    assertEquals(2, result.size());
  }

  @Test
  @DisplayName("Should return throw exception if no active movements found")
  public void testNoHasActiveMovements() throws Exception {
    when(movementsRepository.findAllByStatusIs(Status.ACTIVE)).thenReturn(Collections.emptyList());

    assertThrows(EntityNotFoundException.class, () -> {
      movementsService.listAllActiveMovements();
    });
  }

  @Test
  @DisplayName("Should return a list of all finished movements")
  public void testListAllFinishedMovements() {

    List<MovementsModel> movementsList = Arrays.asList(mockMovements, savedMovements);
    when(movementsRepository.findAllByStatusIs(Status.FINISHED)).thenReturn(movementsList);

    when(movementsMapper.toResponseDTO(mockMovements)).thenReturn(new MovementsResponseDTO(mockEnterpriseResponseDTO, mockVehicleResponseDTO, null, Status.FINISHED, null));
    when(movementsMapper.toResponseDTO(savedMovements)).thenReturn(new MovementsResponseDTO(mockEnterpriseResponseDTO, mockVehicleResponseDTO, null, Status.FINISHED, null));

    List<MovementsResponseDTO> result = movementsService.listAllFinishedMovements();

    assertEquals(2, result.size());
  }

  @Test
  @DisplayName("Should return throw exception if no finished movements found")
  public void testNoHasFinishedMovements() throws Exception {
    when(movementsRepository.findAllByStatusIs(Status.FINISHED)).thenReturn(Collections.emptyList());

    assertThrows(EntityNotFoundException.class, () -> {
      movementsService.listAllFinishedMovements();
    });
  }

  @Test
  @DisplayName("Sould register vehicle exist to change status of movement")
  public void testRegisterExitToChangeStatus() {
    UUID id = UUID.fromString("ad579016-a238-425f-bca7-b59571b5010a");
    mockMovements.setId(id);
    mockMovements.setType(Vehicle.CAR);
    mockEnterprise.setCarSpaces(5);

    when(movementsRepository.findById(id)).thenReturn(Optional.of(mockMovements));
    when(movementsRepository.save(any(MovementsModel.class))).thenReturn(mockMovements);
    when(movementsMapper.toResponseDTO(mockMovements)).thenReturn(expectedResponse);

    MovementsResponseDTO result = movementsService.registerExit(id);

    assertNotNull(result);
    assertEquals(6, mockEnterprise.getCarSpaces());
  }

  @Test
  @DisplayName("Should delete movement by ID")
  public void testDeleteMovementById() {
    mockMovements.setStatus(Status.FINISHED);
    Optional<MovementsModel> movementExist = Optional.of(mockMovements);

    when(movementsRepository.findById(mockMovements.getId())).thenReturn(movementExist);

    movementsService.deleteMovementById(mockMovements.getId());
  }

  @Test
  @DisplayName("Shoud return exception when try delete movement with Status Active")
  public void testTryDeleteActiveMovement() {
    when(movementsRepository.findById(mockMovements.getId())).thenReturn(Optional.of(mockMovements));

    assertThrows(OperationNotPermittedException.class, () -> {
      movementsService.deleteMovementById(mockMovements.getId());
    });
  }
}
