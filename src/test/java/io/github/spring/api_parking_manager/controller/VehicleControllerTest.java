package io.github.spring.api_parking_manager.controller;

import static org.mockito.ArgumentMatchers.any;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.spring.api_parking_manager.exception.EntityNotFoundException;
import io.github.spring.api_parking_manager.model.Vehicle;
import io.github.spring.api_parking_manager.model.VehicleModel;
import io.github.spring.api_parking_manager.model.dtos.VehicleRequestDTO;
import io.github.spring.api_parking_manager.model.dtos.VehicleResponseDTO;
import io.github.spring.api_parking_manager.model.mappers.EnterpriseMapper;
import io.github.spring.api_parking_manager.repository.VehicleRepository;
import io.github.spring.api_parking_manager.service.EnterpriseService;
import io.github.spring.api_parking_manager.service.VehicleService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class VehicleControllerTest {
  
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private VehicleRepository vehicleRepository;

  @MockBean
  private VehicleService vehicleService;

  @MockBean
  private EnterpriseService enterpriseService;

  @MockBean
  private EnterpriseMapper enterpriseMapper;

  private VehicleModel mockVehicle;
  private VehicleRequestDTO mockVehicleRequestDTO;
  private VehicleResponseDTO mockVehicleResponseDTO;
  private VehicleResponseDTO mockVehicleUpdatedDTO;

  @BeforeEach
  void config() {
    mockVehicle = new VehicleModel();
    mockVehicle.setBrand("Honda");
    mockVehicle.setModel("Civic");
    mockVehicle.setColor("Preto");
    mockVehicle.setPlate("ABC1234");
    mockVehicle.setType(Vehicle.CAR);

    mockVehicleRequestDTO = new VehicleRequestDTO(
      "Honda",
      "Civic",
      "Preto",
      "ABC1234",
      Vehicle.CAR
    );

    LocalDateTime now = LocalDateTime.now();

    mockVehicleResponseDTO = new VehicleResponseDTO(
      "Honda",
      "Civic",
      "Preto",
      "ABC1234",
      Vehicle.CAR,
      now,
      now
    );

    mockVehicleUpdatedDTO = new VehicleResponseDTO(
      "Honda",
      "Civic",
      "Vermelho",
      "ABC1234",
      Vehicle.CAR,
      now,
      now
    );
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should register a new vehicle")
  public void testRegisterNewVehicle() throws Exception {
    when(vehicleService.register(any(VehicleModel.class))).thenReturn(mockVehicleResponseDTO);

    mockMvc.perform(post("/api/vehicles")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(mockVehicleRequestDTO)))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.brand").value("Honda"))
      .andExpect(jsonPath("$.model").value("Civic"))
      .andExpect(jsonPath("$.color").value("Preto"))
      .andExpect(jsonPath("$.plate").value("ABC1234"));
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should display all vehicles")
  public void testDisplayAllVehicles() throws Exception {
    List<VehicleResponseDTO> vehicleDTOList = new ArrayList<>();
    vehicleDTOList.add(mockVehicleResponseDTO);

    when(vehicleService.listAllVehicles()).thenReturn(vehicleDTOList);

    mockMvc.perform(get("/api/vehicles")
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath(".[0].brand").value("Honda"))
      .andExpect(jsonPath(".[0].model").value("Civic"))
      .andExpect(jsonPath(".[0].color").value("Preto"))
      .andExpect(jsonPath(".[0].plate").value("ABC1234"));
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should return a exception when no has vehicles registered")
  public void testNoHasVehicles() throws Exception {
    doThrow(EntityNotFoundException.class).when(vehicleService).listAllVehicles();
    mockMvc.perform(get("/api/vehicles"))
           .andExpect(status().isNotFound());
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should find vehicle by ID")
  public void testFindVehicleByID() throws Exception {
    UUID id = UUID.fromString("580ccb15-d277-40b0-8f09-c98210528155");
    when(vehicleService.findVehicleById(id)).thenReturn(Optional.of(mockVehicleResponseDTO));

    mockMvc.perform(get("/api/vehicles/{id}", id)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(mockVehicleRequestDTO)))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.brand").value("Honda"))
      .andExpect(jsonPath("$.model").value("Civic"))
      .andExpect(jsonPath("$.color").value("Preto"))
      .andExpect(jsonPath("$.plate").value("ABC1234"));
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should return a exception when ID not found")
  public void testIdVehicleNotFound() throws Exception {
    UUID id = UUID.fromString("580ccb15-d277-40b0-8f09-c98210528155");
    doThrow(EntityNotFoundException.class).when(vehicleService).findVehicleById(id);
    mockMvc.perform(get("/api/vehicles/{id}", id))
           .andExpect(status().isNotFound());
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should find vehicle by Plate")
  public void testFindVehicleByPlate() throws Exception {
    when(vehicleService.findVehicleByPlate(mockVehicle.getPlate())).thenReturn(Optional.of(mockVehicleResponseDTO));

    String paramName = "plate";
    String paramValue = "ABC1234";

    mockMvc.perform(get("/api/vehicles/plate")
           .param(paramName, paramValue)
           .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.brand").value("Honda"))
      .andExpect(jsonPath("$.model").value("Civic"))
      .andExpect(jsonPath("$.color").value("Preto"))
      .andExpect(jsonPath("$.plate").value("ABC1234"));
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should return a exception when Plate not found")
  public void testPlateNotFound() throws Exception {
    String paramName = "plate";
    String paramValue = "ABC1234";

    doThrow(EntityNotFoundException.class).when(vehicleService).findVehicleByPlate(mockVehicle.getPlate());
    mockMvc.perform(get("/api/vehicles/plate")
            .param(paramName, paramValue))
            .andExpect(status().isNotFound());
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should update vehicle by ID")
  public void testUpdateVehicleById() throws Exception {
    UUID id = UUID.fromString("580ccb15-d277-40b0-8f09-c98210528155");
    mockVehicle.setColor("Vermelho");


    when(vehicleService.updateVehicleById(any(VehicleModel.class)))
        .thenReturn(mockVehicleUpdatedDTO);

    mockMvc.perform(put("/api/vehicles/{id}", id)
           .contentType(MediaType.APPLICATION_JSON)
           .content(objectMapper.writeValueAsString(mockVehicle)))
      .andExpect(jsonPath("$.brand").value("Honda"))
      .andExpect(jsonPath("$.model").value("Civic"))
      .andExpect(jsonPath("$.color").value("Vermelho"))
      .andExpect(jsonPath("$.plate").value("ABC1234"));
  }

  @WithMockUser(username = "admin", roles = {"USER"})
  @Test
  @DisplayName("Should delete vehicle by ID")
  public void testDeleteVehicleById() throws Exception {
    UUID id = UUID.fromString("580ccb15-d277-40b0-8f09-c98210528155");

    doNothing().when(vehicleService).deleteVehicleByID(id);

    mockMvc.perform(delete("/api/vehicles/{id}", id))
          .andExpect(status().isNoContent());
  }
}
