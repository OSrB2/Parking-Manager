package io.github.spring.api_parking_manager.controller;

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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.github.spring.api_parking_manager.exception.EntityNotFoundException;
import io.github.spring.api_parking_manager.model.AddressModel;
import io.github.spring.api_parking_manager.model.EnterpriseModel;
import io.github.spring.api_parking_manager.model.dtos.AddressRequestDTO;
import io.github.spring.api_parking_manager.model.dtos.AddressResponseDTO;
import io.github.spring.api_parking_manager.model.dtos.EnterpriseRequestDTO;
import io.github.spring.api_parking_manager.model.dtos.EnterpriseResponseDTO;
import io.github.spring.api_parking_manager.model.dtos.OccupationDTO;
import io.github.spring.api_parking_manager.model.dtos.ParkingReportDTO;
import io.github.spring.api_parking_manager.model.dtos.VehicleReportDTO;
import io.github.spring.api_parking_manager.model.mappers.EnterpriseMapper;
import io.github.spring.api_parking_manager.model.mappers.MovementsMapper;
import io.github.spring.api_parking_manager.model.mappers.VehicleMapper;
import io.github.spring.api_parking_manager.repository.EnterpriseRepository;
import io.github.spring.api_parking_manager.service.EnterpriseService;
import io.github.spring.api_parking_manager.service.MovementsService;
import io.github.spring.api_parking_manager.service.VehicleService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class EnterpriseControllerTest {
  
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private EnterpriseRepository enterpriseRepository;

  @MockBean
  private EnterpriseService enterpriseService;

  @MockBean
  private VehicleService vehicleService;

  @MockBean
  private VehicleMapper vehicleMapper;

  @MockBean
  private EnterpriseMapper enterpriseMapper;

  @MockBean
  private MovementsService movementsService;

  @MockBean
  private MovementsMapper movementsMapper;

  private EnterpriseModel mockEnterprise;
  private AddressModel mockAddress;
  private EnterpriseRequestDTO mockEnterpriseRequestDTO;
  private EnterpriseResponseDTO mockEnterpriseResponseDTO;
  private EnterpriseResponseDTO mockEnterpriseUpdatedDTO;
  private AddressRequestDTO mockAddressRequestDTO;
  private AddressResponseDTO mockAddressResponseDTO;
  private ParkingReportDTO mockParkingReportDTO;
  private List<VehicleReportDTO> mockVehicleReportDTOList;
  private VehicleReportDTO mockVehicleReportDTO;

  @BeforeEach
  void config() {
    mockAddress = new AddressModel();
    mockAddress.setStreet("Avenida Paulista, 1000");
    mockAddress.setCity("São Paulo");
    mockAddress.setState("SP");
    mockAddress.setPostalCode("01310-100");

    mockAddressResponseDTO = new AddressResponseDTO(
      "Avenida Paulista, 1000",
      "São Paulo",
      "SP",
      "01310-100"
    );

    mockAddressRequestDTO = new AddressRequestDTO(
      "Avenida Paulista, 1000",
      "São Paulo",
      "SP",
      "01310-100"
    );

    mockEnterprise = new EnterpriseModel();
    mockEnterprise.setName("Estaciona Fácil");
    mockEnterprise.setCnpj("63.408.531/0001-38");
    mockEnterprise.setAddress(mockAddress);
    mockEnterprise.setMotorcycleSpaces(10);
    mockEnterprise.setCarSpaces(25);

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

    mockEnterpriseUpdatedDTO = new EnterpriseResponseDTO(
      "Novo Nome",
      "63.408.531/0001-38",
      mockAddressResponseDTO,
      10,
      25
    );

    OccupationDTO carsOccupation = new OccupationDTO(25, 10);
    OccupationDTO motorcyclesOccupation = new OccupationDTO(10, 5);
    mockParkingReportDTO = new ParkingReportDTO(carsOccupation, motorcyclesOccupation);


    mockVehicleReportDTO = new VehicleReportDTO(
      "ABC-1234",
      "CAR",
      LocalDateTime.of(2023, 1, 1, 10, 0, 0),
      LocalDateTime.of(2023, 1, 1, 12, 0, 0),
      "02:00:00"
    );
    
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should register a new enterprise")
  public void testRegisterEnterprise() throws Exception {
    when(enterpriseMapper.toEntity(any(EnterpriseRequestDTO.class))).thenReturn(mockEnterprise);
    when(enterpriseService.register(any(EnterpriseModel.class))).thenReturn(mockEnterpriseResponseDTO);

    mockMvc.perform(post("/api/enterprises")
           .contentType(MediaType.APPLICATION_JSON)
           .content(objectMapper.writeValueAsString(mockEnterpriseRequestDTO)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Estaciona Fácil"))
      .andExpect(jsonPath("$.cnpj").value("63.408.531/0001-38"))
      .andExpect(jsonPath("$.address.street").value("Avenida Paulista, 1000"))
      .andExpect(jsonPath("$.address.city").value("São Paulo"))
      .andExpect(jsonPath("$.address.state").value("SP"))
      .andExpect(jsonPath("$.address.postalCode").value("01310-100"))
      .andExpect(jsonPath("$.motorcycleSpaces").value(10))
      .andExpect(jsonPath("$.carSpaces" ).value(25));
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should display all enterprises")
  public void testListAllEnterprises() throws Exception {
    List<EnterpriseResponseDTO> enterpriseDTOList = new ArrayList<>();
    enterpriseDTOList.add(mockEnterpriseResponseDTO);

    when(enterpriseService.listAllEnterprises()).thenReturn(enterpriseDTOList);

    mockMvc.perform(get("/api/enterprises")
           .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath(".[0].name").value("Estaciona Fácil"))
      .andExpect(jsonPath(".[0].cnpj").value("63.408.531/0001-38"))
      .andExpect(jsonPath(".[0].address.street").value("Avenida Paulista, 1000"))
      .andExpect(jsonPath(".[0].address.city").value("São Paulo"))
      .andExpect(jsonPath(".[0].address.state").value("SP"))
      .andExpect(jsonPath(".[0].address.postalCode").value("01310-100"))
      .andExpect(jsonPath(".[0].motorcycleSpaces").value(10))
      .andExpect(jsonPath(".[0].carSpaces" ).value(25)); 
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should return a exception when no has enterprises registered")
  public void testNoHasEnterprises() throws Exception {
    doThrow(EntityNotFoundException.class).when(enterpriseService).listAllEnterprises();
    mockMvc.perform(get("/api/enterprises"))
           .andExpect(status().isNotFound());
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should find enterprise by ID")
  public void testFindEnterpriseByID() throws Exception {
    UUID id = UUID.fromString("e5d96de2-1a22-4b8c-8256-d689bb57d35b");
    when(enterpriseService.findEntenpriseById(id)).thenReturn(Optional.of(mockEnterpriseResponseDTO));

    mockMvc.perform(get("/api/enterprises/{id}", id)
           .contentType(MediaType.APPLICATION_JSON)
           .content(objectMapper.writeValueAsString(mockEnterpriseRequestDTO)))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.name").value("Estaciona Fácil"))
      .andExpect(jsonPath("$.cnpj").value("63.408.531/0001-38"))
      .andExpect(jsonPath("$.address.street").value("Avenida Paulista, 1000"))
      .andExpect(jsonPath("$.address.city").value("São Paulo"))
      .andExpect(jsonPath("$.address.state").value("SP"))
      .andExpect(jsonPath("$.address.postalCode").value("01310-100"))
      .andExpect(jsonPath("$.motorcycleSpaces").value(10))
      .andExpect(jsonPath("$.carSpaces" ).value(25));     
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should find enterprise by CNPJ")
  public void testFindEnterpriseByCNPJ() throws Exception {
    when(enterpriseService.findEnterpriseByCnpj(mockEnterprise.getCnpj())).thenReturn(Optional.of(mockEnterpriseResponseDTO));

    String paramName = "cnpj";
    String paramValue = "63.408.531/0001-38";

    mockMvc.perform(get("/api/enterprises/cnpj")
           .param(paramName, paramValue)
           .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Estaciona Fácil"))
      .andExpect(jsonPath("$.cnpj").value("63.408.531/0001-38"))
      .andExpect(jsonPath("$.address.street").value("Avenida Paulista, 1000"))
      .andExpect(jsonPath("$.address.city").value("São Paulo"))
      .andExpect(jsonPath("$.address.state").value("SP"))
      .andExpect(jsonPath("$.address.postalCode").value("01310-100"))
      .andExpect(jsonPath("$.motorcycleSpaces").value(10))
      .andExpect(jsonPath("$.carSpaces" ).value(25));     
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should return a exception when CNPJ not found")
  public void testCnpjNotFound() throws Exception {
    String paramName = "cnpj";
    String paramValue = "63.408.531/0001-38";

    doThrow(EntityNotFoundException.class).when(enterpriseService).findEnterpriseByCnpj(mockEnterprise.getCnpj());
    mockMvc.perform(get("/api/enterprises/cnpj")
           .param(paramName, paramValue))
      .andExpect(status().isNotFound());
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should update enterprise by ID")
  public void testUpdateEnterpriseByID() throws Exception {
    UUID id = UUID.fromString("e5d96de2-1a22-4b8c-8256-d689bb57d35b");
    mockEnterprise.setName("Novo Nome");

    when(enterpriseService.updateEnterpriseById(any(EnterpriseModel.class)))
        .thenReturn(mockEnterpriseUpdatedDTO);

    mockMvc.perform(put("/api/enterprises/{id}", id)
           .contentType(MediaType.APPLICATION_JSON)
           .content(objectMapper.writeValueAsString(mockEnterprise)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Novo Nome"))
      .andExpect(jsonPath("$.cnpj").value("63.408.531/0001-38"))
      .andExpect(jsonPath("$.address.street").value("Avenida Paulista, 1000"))
      .andExpect(jsonPath("$.address.city").value("São Paulo"))
      .andExpect(jsonPath("$.address.state").value("SP"))
      .andExpect(jsonPath("$.address.postalCode").value("01310-100"))
      .andExpect(jsonPath("$.motorcycleSpaces").value(10))
      .andExpect(jsonPath("$.carSpaces" ).value(25));  
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should delete enterprise by ID")
  public void testDeleteEnterpriseBYId() throws Exception {
    UUID id = UUID.fromString("e5d96de2-1a22-4b8c-8256-d689bb57d35b");

    doNothing().when(enterpriseService).deleteEnterpriseById(id);

    mockMvc.perform(delete("/api/enterprises/{id}", id))
           .andExpect(status().isNoContent());
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should create a occupation report by enterprise ID")
  public void testCreateOccupationReport() throws Exception {
    UUID id = UUID.fromString("e5d96de2-1a22-4b8c-8256-d689bb57d35b");

    when(enterpriseService.generateOccupancyReport(id)).thenReturn(mockParkingReportDTO);

    mockMvc.perform(get("/api/enterprises/{id}/occupation-report", id)
           .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.cars.totalVacancies").value(25))
      .andExpect(jsonPath("$.cars.vacanciesFilled").value(10))
      .andExpect(jsonPath("$.motorcycles.totalVacancies").value(10))
      .andExpect(jsonPath("$.motorcycles.vacanciesFilled").value(5));
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should create a movement report by enterprise ID")
  public void testCreateMovementReportByEnterpriseId() throws Exception {
    UUID id = UUID.fromString("e5d96de2-1a22-4b8c-8256-d689bb57d35b");

    mockVehicleReportDTOList = new ArrayList<>();
    mockVehicleReportDTOList.add(mockVehicleReportDTO);

    when(enterpriseService.generateMovementReport(id)).thenReturn(mockVehicleReportDTOList);

    mockMvc.perform(get("/api/enterprises/{id}/movement-report", id)
           .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].plate").value("ABC-1234"))
      .andExpect(jsonPath("$[0].type").value("CAR"))
      .andExpect(jsonPath("$[0].entry").value("01/01/2023 10:00"))
      .andExpect(jsonPath("$[0].exit").value("01/01/2023 12:00"))
      .andExpect(jsonPath("$[0].lengthStay").value("02:00:00"));
  }
}
