package io.github.spring.api_parking_manager.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import io.github.spring.api_parking_manager.exception.EntityNotFoundException;
import io.github.spring.api_parking_manager.model.AddressModel;
import io.github.spring.api_parking_manager.model.EnterpriseModel;
import io.github.spring.api_parking_manager.model.MovementsModel;
import io.github.spring.api_parking_manager.model.Status;
import io.github.spring.api_parking_manager.model.Vehicle;
import io.github.spring.api_parking_manager.model.VehicleModel;
import io.github.spring.api_parking_manager.model.dtos.AddressRequestDTO;
import io.github.spring.api_parking_manager.model.dtos.AddressResponseDTO;
import io.github.spring.api_parking_manager.model.dtos.EnterpriseRequestDTO;
import io.github.spring.api_parking_manager.model.dtos.EnterpriseResponseDTO;
import io.github.spring.api_parking_manager.model.dtos.MovementsRequestDTO;
import io.github.spring.api_parking_manager.model.dtos.MovementsResponseDTO;
import io.github.spring.api_parking_manager.model.dtos.VehicleRequestDTO;
import io.github.spring.api_parking_manager.model.dtos.VehicleResponseDTO;
import io.github.spring.api_parking_manager.model.mappers.EnterpriseMapper;
import io.github.spring.api_parking_manager.model.mappers.MovementsMapper;
import io.github.spring.api_parking_manager.model.mappers.VehicleMapper;
import io.github.spring.api_parking_manager.repository.MovementsRepository;
import io.github.spring.api_parking_manager.service.MovementsService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class MovementControllerTest {
  
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private MovementsRepository movementsRepository;

  @MockBean
  private MovementsService movementsService;

  @MockBean
  private MovementsMapper movementsMapper;

  @MockBean
  private EnterpriseMapper enterpriseMapper;

  @MockBean
  private VehicleMapper moVehicleMapper;

  private MovementsModel mockMovements;
  private MovementsRequestDTO mockMovementsRequestDTO;
  private MovementsResponseDTO mockMovementsResponseDTO;
  private MovementsResponseDTO mockFinishedMovementResponseDTO;
  private AddressModel mockAddressModel;
  private AddressRequestDTO mockAddressRequestDTO;
  private AddressResponseDTO mockAddressResponseDTO;
  private VehicleModel mockVehicleModel;
  private VehicleRequestDTO mockVehicleRequestDTO;
  private VehicleResponseDTO mockVehicleResponseDTO;
  private EnterpriseModel mockEnterpriseModel;
  private EnterpriseRequestDTO mockEnterpriseRequestDTO;
  private EnterpriseResponseDTO mockEnterpriseResponseDTO;

  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

  @BeforeEach
  void config() {

    mockAddressModel = new AddressModel();
    mockAddressModel.setStreet("Avenida Paulista, 1000");
    mockAddressModel.setCity("São Paulo");
    mockAddressModel.setState("SP");
    mockAddressModel.setPostalCode("01310-100");

    mockAddressRequestDTO = new AddressRequestDTO(
      "Avenida Paulista, 1000",
      "São Paulo",
      "SP",
      "01310-100"
    );

    mockAddressResponseDTO = new AddressResponseDTO(
      "Avenida Paulista, 1000",
      "São Paulo",
      "SP",
      "01310-100"
    );

    mockEnterpriseModel = new EnterpriseModel();
    mockEnterpriseModel.setName("Estaciona Fácil");
    mockEnterpriseModel.setCnpj("63.408.531/0001-38");
    mockEnterpriseModel.setAddress(mockAddressModel);
    mockEnterpriseModel.setMotorcycleSpaces(10);
    mockEnterpriseModel.setCarSpaces(25);

    mockEnterpriseRequestDTO = new EnterpriseRequestDTO(
      "Estaciona Fácil",
      "63.408.531/0001-38",
      mockAddressRequestDTO,
      10,
      25
    );

    mockEnterpriseResponseDTO = new EnterpriseResponseDTO(
      "Estaciona Fácil",
      "63.408.531/0001-38",
      mockAddressResponseDTO,
      10,
      25
    );


    mockVehicleModel = new VehicleModel();
    mockVehicleModel.setBrand("Honda");
    mockVehicleModel.setModel("Civic");
    mockVehicleModel.setColor("Color");
    mockVehicleModel.setPlate("ABC1234");
    mockVehicleModel.setType(Vehicle.CAR);

    mockVehicleRequestDTO = new VehicleRequestDTO(
      "Honda",
      "Civic",
      "Preto",
      "ABC1234",
      Vehicle.CAR
    );

    mockVehicleResponseDTO = new VehicleResponseDTO(
      "Honda",
      "Civic",
      "Preto",
      "ABC1234",
      Vehicle.CAR,
      LocalDateTime.now(),
      LocalDateTime.now()
    );

    mockMovements = new MovementsModel();
    mockMovements.setVehicle(mockVehicleModel);
    mockMovements.setEnterprise(mockEnterpriseModel);
    mockMovements.setEntryTime(LocalDateTime.now());
    mockMovements.setDepartureTime(null);
    mockMovements.setStatus(Status.ACTIVE);
    mockMovements.setType(Vehicle.CAR);
    mockMovements.setCreatedAt(LocalDateTime.now());
    mockMovements.setUpdatedAt(LocalDateTime.now());

    mockMovementsResponseDTO = new MovementsResponseDTO(
      mockEnterpriseResponseDTO, 
      mockVehicleResponseDTO, 
      LocalDateTime.now(), 
      Status.ACTIVE, 
      null
    );

    mockFinishedMovementResponseDTO = new MovementsResponseDTO(
      mockEnterpriseResponseDTO, 
      mockVehicleResponseDTO, 
      LocalDateTime.now(), 
      Status.FINISHED, 
      LocalDateTime.now()
    );

  }


  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should register a new movement")
  public void testRegisterNewMovement() throws Exception {
    UUID vehicleID = UUID.randomUUID();
    UUID enterpriseID = UUID.randomUUID();
    mockVehicleModel.setId(vehicleID);
    mockEnterpriseModel.setId(enterpriseID);

    mockMovementsRequestDTO = new MovementsRequestDTO(mockVehicleModel.getId(), mockEnterpriseModel.getId());

    when(movementsMapper.toEntity(any(MovementsRequestDTO.class))).thenReturn(mockMovements);
    when(movementsService.registerEntry(vehicleID, enterpriseID)).thenReturn(mockMovementsResponseDTO);

    mockMvc.perform(post("/api/movements")
           .contentType(MediaType.APPLICATION_JSON)
           .content(objectMapper.writeValueAsString(mockMovementsRequestDTO)))
           .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.enterprise.name").value(mockMovementsResponseDTO.enterprise().name()))
      .andExpect(jsonPath("$.enterprise.cnpj").value(mockMovementsResponseDTO.enterprise().cnpj()))
      .andExpect(jsonPath("$.enterprise.address.street").value(mockMovementsResponseDTO.enterprise().address().street()))
      .andExpect(jsonPath("$.enterprise.address.city").value(mockMovementsResponseDTO.enterprise().address().city()))
      .andExpect(jsonPath("$.enterprise.address.state").value(mockMovementsResponseDTO.enterprise().address().state()))
      .andExpect(jsonPath("$.enterprise.address.postalCode").value(mockMovementsResponseDTO.enterprise().address().postalCode()))
      .andExpect(jsonPath("$.enterprise.motorcycleSpaces").value(mockMovementsResponseDTO.enterprise().motorcycleSpaces()))
      .andExpect(jsonPath("$.enterprise.carSpaces").value(mockMovementsResponseDTO.enterprise().carSpaces()))
      .andExpect(jsonPath("$.vehicle.brand").value(mockMovementsResponseDTO.vehicle().brand()))
      .andExpect(jsonPath("$.vehicle.model").value(mockMovementsResponseDTO.vehicle().model()))
      .andExpect(jsonPath("$.vehicle.color").value(mockMovementsResponseDTO.vehicle().color()))
      .andExpect(jsonPath("$.vehicle.plate").value(mockMovementsResponseDTO.vehicle().plate()))
      .andExpect(jsonPath("$.vehicle.type").value(mockMovementsResponseDTO.vehicle().type().toString()))
      .andExpect(jsonPath("$.vehicle.createdAt").value(mockMovementsResponseDTO.vehicle().createdAt().format(formatter)))
      .andExpect(jsonPath("$.vehicle.updatedAt").value(mockMovementsResponseDTO.vehicle().updatedAt().format(formatter)))
      .andExpect(jsonPath("$.entryTime").value(mockMovementsResponseDTO.entryTime().format(formatter)))
      .andExpect(jsonPath("$.status").value(mockMovementsResponseDTO.status().toString()))
      .andExpect(jsonPath("$.departureTime").value(mockMovementsResponseDTO.departureTime()));
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should display all movements")
  public void testListAllMovements() throws Exception {
    List<MovementsResponseDTO> movementsDTOList = new ArrayList<>();
    movementsDTOList.add(mockMovementsResponseDTO);

    when(movementsService.listAllMovements()).thenReturn(movementsDTOList);

    mockMvc.perform(get("/api/movements")
           .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
      .andExpect(jsonPath(".[0].enterprise.name").value(mockMovementsResponseDTO.enterprise().name()))
      .andExpect(jsonPath(".[0].enterprise.cnpj").value(mockMovementsResponseDTO.enterprise().cnpj()))
      .andExpect(jsonPath(".[0].enterprise.address.street").value(mockMovementsResponseDTO.enterprise().address().street()))
      .andExpect(jsonPath(".[0].enterprise.address.city").value(mockMovementsResponseDTO.enterprise().address().city()))
      .andExpect(jsonPath(".[0].enterprise.address.state").value(mockMovementsResponseDTO.enterprise().address().state()))
      .andExpect(jsonPath(".[0].enterprise.address.postalCode").value(mockMovementsResponseDTO.enterprise().address().postalCode()))
      .andExpect(jsonPath(".[0].enterprise.motorcycleSpaces").value(mockMovementsResponseDTO.enterprise().motorcycleSpaces()))
      .andExpect(jsonPath(".[0].enterprise.carSpaces").value(mockMovementsResponseDTO.enterprise().carSpaces()))
      .andExpect(jsonPath(".[0].vehicle.brand").value(mockMovementsResponseDTO.vehicle().brand()))
      .andExpect(jsonPath(".[0].vehicle.model").value(mockMovementsResponseDTO.vehicle().model()))
      .andExpect(jsonPath(".[0].vehicle.color").value(mockMovementsResponseDTO.vehicle().color()))
      .andExpect(jsonPath(".[0].vehicle.plate").value(mockMovementsResponseDTO.vehicle().plate()))
      .andExpect(jsonPath(".[0].vehicle.type").value(mockMovementsResponseDTO.vehicle().type().toString()))
      .andExpect(jsonPath(".[0].vehicle.createdAt").value(mockMovementsResponseDTO.vehicle().createdAt().format(formatter)))
      .andExpect(jsonPath(".[0].vehicle.updatedAt").value(mockMovementsResponseDTO.vehicle().updatedAt().format(formatter)))
      .andExpect(jsonPath(".[0].entryTime").value(mockMovementsResponseDTO.entryTime().format(formatter)))
      .andExpect(jsonPath(".[0].status").value(mockMovementsResponseDTO.status().toString()))
      .andExpect(jsonPath(".[0].departureTime").value(mockMovementsResponseDTO.departureTime()));
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should return a exception when no has movements registered")
  public void testNoHasMovements() throws Exception {
    doThrow(EntityNotFoundException.class).when(movementsService).listAllMovements();
    mockMvc.perform(get("/api/movements"))
           .andExpect(status().isNotFound());
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should list all active movements")
  public void testListAllActiveMovements() throws Exception {
    List<MovementsResponseDTO> movementsDTOList = new ArrayList<>();
    movementsDTOList.add(mockMovementsResponseDTO);
    when(movementsService.listAllActiveMovements()).thenReturn(movementsDTOList);

    mockMvc.perform(get("/api/movements/active")
           .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
      .andExpect(jsonPath(".[0].enterprise.name").value(mockMovementsResponseDTO.enterprise().name()))
      .andExpect(jsonPath(".[0].enterprise.cnpj").value(mockMovementsResponseDTO.enterprise().cnpj()))
      .andExpect(jsonPath(".[0].enterprise.address.street").value(mockMovementsResponseDTO.enterprise().address().street()))
      .andExpect(jsonPath(".[0].enterprise.address.city").value(mockMovementsResponseDTO.enterprise().address().city()))
      .andExpect(jsonPath(".[0].enterprise.address.state").value(mockMovementsResponseDTO.enterprise().address().state()))
      .andExpect(jsonPath(".[0].enterprise.address.postalCode").value(mockMovementsResponseDTO.enterprise().address().postalCode()))
      .andExpect(jsonPath(".[0].enterprise.motorcycleSpaces").value(mockMovementsResponseDTO.enterprise().motorcycleSpaces()))
      .andExpect(jsonPath(".[0].enterprise.carSpaces").value(mockMovementsResponseDTO.enterprise().carSpaces()))
      .andExpect(jsonPath(".[0].vehicle.brand").value(mockMovementsResponseDTO.vehicle().brand()))
      .andExpect(jsonPath(".[0].vehicle.model").value(mockMovementsResponseDTO.vehicle().model()))
      .andExpect(jsonPath(".[0].vehicle.color").value(mockMovementsResponseDTO.vehicle().color()))
      .andExpect(jsonPath(".[0].vehicle.plate").value(mockMovementsResponseDTO.vehicle().plate()))
      .andExpect(jsonPath(".[0].vehicle.type").value(mockMovementsResponseDTO.vehicle().type().toString()))
      .andExpect(jsonPath(".[0].vehicle.createdAt").value(mockMovementsResponseDTO.vehicle().createdAt().format(formatter)))
      .andExpect(jsonPath(".[0].vehicle.updatedAt").value(mockMovementsResponseDTO.vehicle().updatedAt().format(formatter)))
      .andExpect(jsonPath(".[0].entryTime").value(mockMovementsResponseDTO.entryTime().format(formatter)))
      .andExpect(jsonPath(".[0].status").value(mockMovementsResponseDTO.status().toString()))
      .andExpect(jsonPath(".[0].departureTime").value(mockMovementsResponseDTO.departureTime()));
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  public void testFindFinishedMovements() throws Exception {

    List<MovementsResponseDTO> movementsDTOList = new ArrayList<>();
    movementsDTOList.add(mockFinishedMovementResponseDTO);
    when(movementsService.listAllActiveMovements()).thenReturn(movementsDTOList);

    mockMvc.perform(get("/api/movements/active")
           .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
      .andExpect(jsonPath(".[0].enterprise.name").value(mockFinishedMovementResponseDTO.enterprise().name()))
      .andExpect(jsonPath(".[0].enterprise.cnpj").value(mockFinishedMovementResponseDTO.enterprise().cnpj()))
      .andExpect(jsonPath(".[0].enterprise.address.street").value(mockFinishedMovementResponseDTO.enterprise().address().street()))
      .andExpect(jsonPath(".[0].enterprise.address.city").value(mockFinishedMovementResponseDTO.enterprise().address().city()))
      .andExpect(jsonPath(".[0].enterprise.address.state").value(mockFinishedMovementResponseDTO.enterprise().address().state()))
      .andExpect(jsonPath(".[0].enterprise.address.postalCode").value(mockFinishedMovementResponseDTO.enterprise().address().postalCode()))
      .andExpect(jsonPath(".[0].enterprise.motorcycleSpaces").value(mockFinishedMovementResponseDTO.enterprise().motorcycleSpaces()))
      .andExpect(jsonPath(".[0].enterprise.carSpaces").value(mockFinishedMovementResponseDTO.enterprise().carSpaces()))
      .andExpect(jsonPath(".[0].vehicle.brand").value(mockFinishedMovementResponseDTO.vehicle().brand()))
      .andExpect(jsonPath(".[0].vehicle.model").value(mockFinishedMovementResponseDTO.vehicle().model()))
      .andExpect(jsonPath(".[0].vehicle.color").value(mockFinishedMovementResponseDTO.vehicle().color()))
      .andExpect(jsonPath(".[0].vehicle.plate").value(mockFinishedMovementResponseDTO.vehicle().plate()))
      .andExpect(jsonPath(".[0].vehicle.type").value(mockFinishedMovementResponseDTO.vehicle().type().toString()))
      .andExpect(jsonPath(".[0].vehicle.createdAt").value(mockFinishedMovementResponseDTO.vehicle().createdAt().format(formatter)))
      .andExpect(jsonPath(".[0].vehicle.updatedAt").value(mockFinishedMovementResponseDTO.vehicle().updatedAt().format(formatter)))
      .andExpect(jsonPath(".[0].entryTime").value(mockFinishedMovementResponseDTO.entryTime().format(formatter)))
      .andExpect(jsonPath(".[0].status").value(mockFinishedMovementResponseDTO.status().toString()))
      .andExpect(jsonPath(".[0].departureTime").value(mockFinishedMovementResponseDTO.departureTime().format(formatter)));
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should find movement by ID")
  public void testFindMovementByID() throws Exception {
    UUID id = UUID.fromString("487b5968-e7dd-4b24-9d85-a051e2774956");
    when(movementsService.findMovementById(id)).thenReturn(Optional.of(mockMovementsResponseDTO));

    mockMvc.perform(get("/api/movements/{id}", id))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.enterprise.name").value(mockMovementsResponseDTO.enterprise().name()))
      .andExpect(jsonPath("$.enterprise.cnpj").value(mockMovementsResponseDTO.enterprise().cnpj()))
      .andExpect(jsonPath("$.enterprise.address.street").value(mockMovementsResponseDTO.enterprise().address().street()))
      .andExpect(jsonPath("$.enterprise.address.city").value(mockMovementsResponseDTO.enterprise().address().city()))
      .andExpect(jsonPath("$.enterprise.address.state").value(mockMovementsResponseDTO.enterprise().address().state()))
      .andExpect(jsonPath("$.enterprise.address.postalCode").value(mockMovementsResponseDTO.enterprise().address().postalCode()))
      .andExpect(jsonPath("$.enterprise.motorcycleSpaces").value(mockMovementsResponseDTO.enterprise().motorcycleSpaces()))
      .andExpect(jsonPath("$.enterprise.carSpaces").value(mockMovementsResponseDTO.enterprise().carSpaces()))
      .andExpect(jsonPath("$.vehicle.brand").value(mockMovementsResponseDTO.vehicle().brand()))
      .andExpect(jsonPath("$.vehicle.model").value(mockMovementsResponseDTO.vehicle().model()))
      .andExpect(jsonPath("$.vehicle.color").value(mockMovementsResponseDTO.vehicle().color()))
      .andExpect(jsonPath("$.vehicle.plate").value(mockMovementsResponseDTO.vehicle().plate()))
      .andExpect(jsonPath("$.vehicle.type").value(mockMovementsResponseDTO.vehicle().type().toString()))
      .andExpect(jsonPath("$.vehicle.createdAt").value(mockMovementsResponseDTO.vehicle().createdAt().format(formatter)))
      .andExpect(jsonPath("$.vehicle.updatedAt").value(mockMovementsResponseDTO.vehicle().updatedAt().format(formatter)))
      .andExpect(jsonPath("$.entryTime").value(mockMovementsResponseDTO.entryTime().format(formatter)))
      .andExpect(jsonPath("$.status").value(mockMovementsResponseDTO.status().toString()))
      .andExpect(jsonPath("$.departureTime").value(mockMovementsResponseDTO.departureTime()));
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should register exit vehicle from enterprise")
  public void testRegisterExist() throws Exception {
    UUID id = UUID.fromString("487b5968-e7dd-4b24-9d85-a051e2774956");
    when(movementsService.registerExit(id)).thenReturn(mockFinishedMovementResponseDTO);

    mockMvc.perform(put("/api/movements/{id}", id))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.enterprise.name").value(mockFinishedMovementResponseDTO.enterprise().name()))
      .andExpect(jsonPath("$.enterprise.cnpj").value(mockFinishedMovementResponseDTO.enterprise().cnpj()))
      .andExpect(jsonPath("$.enterprise.address.street").value(mockFinishedMovementResponseDTO.enterprise().address().street()))
      .andExpect(jsonPath("$.enterprise.address.city").value(mockFinishedMovementResponseDTO.enterprise().address().city()))
      .andExpect(jsonPath("$.enterprise.address.state").value(mockFinishedMovementResponseDTO.enterprise().address().state()))
      .andExpect(jsonPath("$.enterprise.address.postalCode").value(mockFinishedMovementResponseDTO.enterprise().address().postalCode()))
      .andExpect(jsonPath("$.enterprise.motorcycleSpaces").value(mockFinishedMovementResponseDTO.enterprise().motorcycleSpaces()))
      .andExpect(jsonPath("$.enterprise.carSpaces").value(mockFinishedMovementResponseDTO.enterprise().carSpaces()))
      .andExpect(jsonPath("$.vehicle.brand").value(mockFinishedMovementResponseDTO.vehicle().brand()))
      .andExpect(jsonPath("$.vehicle.model").value(mockFinishedMovementResponseDTO.vehicle().model()))
      .andExpect(jsonPath("$.vehicle.color").value(mockFinishedMovementResponseDTO.vehicle().color()))
      .andExpect(jsonPath("$.vehicle.plate").value(mockFinishedMovementResponseDTO.vehicle().plate()))
      .andExpect(jsonPath("$.vehicle.type").value(mockFinishedMovementResponseDTO.vehicle().type().toString()))
      .andExpect(jsonPath("$.vehicle.createdAt").value(mockFinishedMovementResponseDTO.vehicle().createdAt().format(formatter)))
      .andExpect(jsonPath("$.vehicle.updatedAt").value(mockFinishedMovementResponseDTO.vehicle().updatedAt().format(formatter)))
      .andExpect(jsonPath("$.entryTime").value(mockFinishedMovementResponseDTO.entryTime().format(formatter)))
      .andExpect(jsonPath("$.status").value(mockFinishedMovementResponseDTO.status().toString()))
      .andExpect(jsonPath("$.departureTime").value(mockFinishedMovementResponseDTO.departureTime().format(formatter)));
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  public void testDeleteMovementById() throws Exception {
    UUID id = UUID.fromString("487b5968-e7dd-4b24-9d85-a051e2774956");

    doNothing().when(movementsService).deleteMovementById(id);

    mockMvc.perform(delete("/api/movements/{id}", id))
           .andExpect(status().isNoContent());
  }
}
