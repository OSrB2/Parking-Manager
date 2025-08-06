package io.github.spring.api_parking_manager.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.spring.api_parking_manager.exception.EntityNotFoundException;
import io.github.spring.api_parking_manager.model.AddressModel;
import io.github.spring.api_parking_manager.model.EnterpriseModel;
import io.github.spring.api_parking_manager.model.MovementsModel;
import io.github.spring.api_parking_manager.model.Vehicle;
import io.github.spring.api_parking_manager.model.VehicleModel;
import io.github.spring.api_parking_manager.model.dtos.ActiveVehicleDTO;
import io.github.spring.api_parking_manager.model.dtos.AddressResponseDTO;
import io.github.spring.api_parking_manager.model.dtos.EnterpriseResponseDTO;
import io.github.spring.api_parking_manager.model.dtos.ParkingReportDTO;
import io.github.spring.api_parking_manager.model.dtos.VehicleReportDTO;
import io.github.spring.api_parking_manager.model.mappers.AddressMapper;
import io.github.spring.api_parking_manager.model.mappers.EnterpriseMapper;
import io.github.spring.api_parking_manager.repository.EnterpriseRepository;
import io.github.spring.api_parking_manager.repository.MovementsRepository;
import io.github.spring.api_parking_manager.validator.EnterpriseValidator;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class EnterpriseServeceTest {
  @Mock
  private EnterpriseRepository enterpriseRepository;

  @Mock
  private MovementsRepository movementsRepository;

  @Mock
  private EnterpriseMapper enterpriseMapper;

  @Mock
  private AddressMapper addressMapper;

  @Mock
  private EnterpriseValidator validator;

  @Mock
  private MovementsService movementsService;

  @InjectMocks
  private EnterpriseService enterpriseService;

  private EnterpriseModel mockEnterprise;
  private EnterpriseModel savedEnterprise;
  private EnterpriseModel updateEnterprise;
  private EnterpriseResponseDTO mockResponseDTO;
  private AddressResponseDTO addressResponseDTO;
  private AddressModel mockAddress;
  private VehicleModel mockVehicle;
  private MovementsModel mockMovement;

  @BeforeEach
    void config() {
    mockAddress = new AddressModel();
    mockAddress.setStreet("Avenida Paulista");
    mockAddress.setCity("São Paulo");
    mockAddress.setState("SP");
    mockAddress.setPostalCode("01310-100");

    addressResponseDTO = new AddressResponseDTO(
      mockAddress.getStreet(),
      mockAddress.getCity(),
      mockAddress.getState(),
      mockAddress.getPostalCode()
    );

    mockEnterprise = new EnterpriseModel();
    mockEnterprise.setName("Estaciona Fácil");
    mockEnterprise.setCnpj("63.408.531/0001-38");
    mockEnterprise.setAddress(mockAddress);
    mockEnterprise.setMotorcycleSpaces(10);
    mockEnterprise.setCarSpaces(25);

    mockResponseDTO = new EnterpriseResponseDTO(
        "Estaciona Fácil",
        "63.408.531/0001-38",
        addressResponseDTO,
        10,
        25
    );

    savedEnterprise = new EnterpriseModel();
    savedEnterprise.setName("Estaciona Fácil");
    savedEnterprise.setCnpj("63.408.531/0001-38");
    savedEnterprise.setAddress(mockAddress);
    savedEnterprise.setMotorcycleSpaces(10);
    savedEnterprise.setCarSpaces(25);

    updateEnterprise = new EnterpriseModel();
    updateEnterprise.setName("Estaciona Fácil Novo Nome");
    updateEnterprise.setCnpj("63.408.531/0001-38");
    updateEnterprise.setAddress(mockAddress);
    updateEnterprise.setMotorcycleSpaces(10);
    updateEnterprise.setCarSpaces(25);

    mockVehicle = new VehicleModel();
    mockVehicle.setPlate("ABC1D23");
    mockVehicle.setType(Vehicle.CAR);
    mockVehicle.setColor("Preto");

    mockMovement = new MovementsModel();
    mockMovement.setId(UUID.randomUUID());
    mockMovement.setEnterprise(mockEnterprise);
    mockMovement.setVehicle(mockVehicle);
    mockMovement.setEntryTime(LocalDateTime.of(2025, 8, 1, 10, 0));
    mockMovement.setDepartureTime(LocalDateTime.of(2025, 8, 1, 12, 0));
  }
  
  @Test
  @DisplayName("Should register a new enterprise")
  public void testRegisterNewEnterprise() {
    doNothing().when(validator).validate(mockEnterprise);
    when(enterpriseRepository.save(any(EnterpriseModel.class))).thenReturn(mockEnterprise);
    when(enterpriseMapper.toResponseDTO(mockEnterprise)).thenReturn(mockResponseDTO);

    EnterpriseResponseDTO result = enterpriseService.register(mockEnterprise);

    assertNotNull(result);
    assertEquals(mockResponseDTO, result);
    verify(validator).validate(mockEnterprise);
    verify(enterpriseRepository).save(mockEnterprise);
    verify(enterpriseMapper).toResponseDTO(mockEnterprise);
  }

  @Test
  @DisplayName("Should return a list of enterprises")
  public void testListAllEnterprises() {
    List<EnterpriseModel> enterpriseList = Arrays.asList(mockEnterprise, savedEnterprise);

    when(enterpriseRepository.findAll()).thenReturn(enterpriseList);

    List<EnterpriseResponseDTO> enterpriseResponseDTOList = enterpriseService.listAllEnterprises();

    assertEquals(2, enterpriseResponseDTOList.size());
  }

  @Test
  @DisplayName("Should throw exception if there are no enterprises")
  public void testNoHasEnterprises() {
    when(enterpriseRepository.findAll()).thenReturn(new ArrayList<>());

    assertThrows(EntityNotFoundException.class, () -> enterpriseService.listAllEnterprises());
  }

  @Test
  @DisplayName("Should return a enterprise by ID")
  public void testFindEnterpriseById() {
    UUID id = UUID.fromString("2e1e0fdb-ea0a-471a-94fc-6bdb0e688a12");
    mockEnterprise.setId(id);

    when(enterpriseRepository.findById(id)).thenReturn(Optional.of(mockEnterprise));
    when(enterpriseMapper.toResponseDTO(mockEnterprise)).thenReturn(mockResponseDTO);

    Optional<EnterpriseResponseDTO> result = enterpriseService.findEntenpriseById(id);

    assertTrue(result.isPresent());
    assertEquals(mockResponseDTO, result.get());
  }

  @Test
  @DisplayName("Should return exception when id not found")
  public void testIdNotFound() throws Exception {
    UUID id = UUID.fromString("2e1e0fdb-ea0a-471a-94fc-6bdb0e688a12");

    when(enterpriseRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> {
      enterpriseService.findEntenpriseById(id);
    });
  }

  @Test
  @DisplayName("Should return a enterprise by CNPJ")
  public void testFindEnterpriseByCnpj() {
    when(enterpriseRepository.findByCnpj(mockEnterprise.getCnpj())).thenReturn(Optional.of(mockEnterprise));
    when(enterpriseMapper.toResponseDTO(mockEnterprise)).thenReturn(mockResponseDTO);

    Optional<EnterpriseResponseDTO> result = enterpriseService.findEnterpriseByCnpj(mockEnterprise.getCnpj());

    assertTrue(result.isPresent());
    assertEquals(mockResponseDTO, result.get());
  }

  @Test
  @DisplayName("Shoul return exception when cnpj not found")
  public void testCnpjNotFound() throws Exception {
    when(enterpriseRepository.findByCnpj(mockEnterprise.getCnpj())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> {
      enterpriseService.findEnterpriseByCnpj(mockEnterprise.getCnpj());
    });
  }

  @Test
  @DisplayName("Should update enterprise by ID")
  public void testUpdateEnterpriseById() {
    UUID id = UUID.fromString("2e1e0fdb-ea0a-471a-94fc-6bdb0e688a12");

    mockEnterprise.setId(id);
    updateEnterprise.setId(id);

    AddressResponseDTO addressDTO = addressMapper.tResponseDTO(updateEnterprise.getAddress());

    EnterpriseResponseDTO updateEnterpriseDTO = new EnterpriseResponseDTO(
        updateEnterprise.getName(),
        updateEnterprise.getCnpj(),
        addressDTO,
        updateEnterprise.getMotorcycleSpaces(),
        updateEnterprise.getCarSpaces()
    );

    when(enterpriseRepository.findById(id)).thenReturn(Optional.of(mockEnterprise));
    when(enterpriseRepository.save(any(EnterpriseModel.class))).thenReturn(mockEnterprise);
    when(enterpriseMapper.toResponseDTO(mockEnterprise)).thenReturn(updateEnterpriseDTO);

    EnterpriseResponseDTO result = enterpriseService.updateEnterpriseById(mockEnterprise);

    assertEquals("Estaciona Fácil Novo Nome", result.name());
    assertEquals(mockEnterprise.getId(), id);

    verify(enterpriseRepository).findById(id);
    verify(enterpriseRepository).save(mockEnterprise);
    verify(enterpriseMapper).toResponseDTO(mockEnterprise);
  }

  @Test
  @DisplayName("Should delete enterprise by ID")
  public void testDeleteEnterpriseById() {
    Optional<EnterpriseModel> enterpriseExist = Optional.of(mockEnterprise);

    when(enterpriseRepository.findById(mockEnterprise.getId())).thenReturn(enterpriseExist);

    enterpriseService.deleteEnterpriseById(mockEnterprise.getId());
  }

  @Test
  @DisplayName("Should generate occupancy report correctly")
  public void testGenerateOcuppancyReport() {
    UUID id = UUID.fromString("2e1e0fdb-ea0a-471a-94fc-6bdb0e688a12");
    mockEnterprise.setId(id);

    when(enterpriseRepository.findById(id)).thenReturn(Optional.of(mockEnterprise));
    when(movementsRepository.countParkedCars(id)).thenReturn(7L);
    when(movementsRepository.countParkedMotorcycles(id)).thenReturn(3L);

    ParkingReportDTO result = enterpriseService.generateOccupancyReport(id);

    assertNotNull(result);
    assertEquals(25, result.cars().totalVacancies());
    assertEquals(7, result.cars().vacanciesFilled());
    assertEquals(10, result.motorcycles().totalVacancies());
    assertEquals(3, result.motorcycles().vacanciesFilled());
  }

  @Test
  @DisplayName("Should generate movement report") 
  public void testGenerateMovementReport() {
    UUID id = UUID.fromString("2e1e0fdb-ea0a-471a-94fc-6bdb0e688a12");

    when(movementsRepository.findByEnterpriseId(id)).thenReturn(List.of(mockMovement));

    List<VehicleReportDTO> result = enterpriseService.generateMovementReport(id);

    assertEquals(1, result.size());
  }

  @Test
  @DisplayName("Should list all active vehicles by enterprise ID")
  public void testListAllActiveVehiclesByEnterpriseID() {
    UUID id = UUID.fromString("2e1e0fdb-ea0a-471a-94fc-6bdb0e688a12");

    when(movementsRepository.findByIdCarsParkedOnEnterprise(id)).thenReturn(List.of(mockMovement));

    List<ActiveVehicleDTO> result = enterpriseService.listActiveVehicles(id);

    assertNotNull(result);
    assertEquals(1, result.size());
  }
}
