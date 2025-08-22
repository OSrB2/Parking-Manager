package io.github.spring.api_parking_manager.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.spring.api_parking_manager.exception.ResponseError;
import io.github.spring.api_parking_manager.model.EnterpriseModel;
import io.github.spring.api_parking_manager.model.dtos.ActiveVehicleDTO;
import io.github.spring.api_parking_manager.model.dtos.EnterpriseRequestDTO;
import io.github.spring.api_parking_manager.model.dtos.EnterpriseResponseDTO;
import io.github.spring.api_parking_manager.model.dtos.ParkingReportDTO;
import io.github.spring.api_parking_manager.model.dtos.VehicleReportDTO;
import io.github.spring.api_parking_manager.model.mappers.EnterpriseMapper;
import io.github.spring.api_parking_manager.service.EnterpriseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/enterprises")
@RequiredArgsConstructor
@Tag(name = "Enterprises")
public class EnterpriseController {
  
  private final EnterpriseService enterpriseService;
  private final EnterpriseMapper enterpriseMapper;

  @PostMapping
  @Operation(summary = "Register a new enterprise", description = "Create and register a new enterprise in the system.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Enterprise successfully registered.",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = EnterpriseResponseDTO.class)
      )),
    @ApiResponse(responseCode = "400", description = "Bad request",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = ResponseError.class))),
    @ApiResponse(responseCode = "409", description = "Conflict. Return a message if the CNPJ is already registered.",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = ResponseError.class))),
    @ApiResponse(responseCode = "422", description = "Validation error",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = ResponseError.class)))
  })
  public ResponseEntity<EnterpriseResponseDTO> registerEnterprise(@RequestBody @Valid EnterpriseRequestDTO enterpriseRequestDTO) {
    EnterpriseModel enterprise = enterpriseMapper.toEntity(enterpriseRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(enterpriseService.register(enterprise));
  }

  @GetMapping
  @Operation(summary = "List all enterprises", description = "Retrieve a list of all enterprises currently registered in the system.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "List of enterprises successfully retrieved.",
      content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = EnterpriseResponseDTO.class)
    )),
    @ApiResponse(responseCode = "400", description = "Bad request",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class))),
    @ApiResponse(responseCode = "404", description = "Not found",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class)))
  })
  public ResponseEntity<List<EnterpriseResponseDTO>> listAll() {
    return ResponseEntity.ok(enterpriseService.listAllEnterprises());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Find a enterprise by ID", description = "Returns deiled information of a specific enterprise identified by its unique UUID.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Enterprise retrieved successfully."),
    @ApiResponse(responseCode = "400", description = "Invalid UUID format provided.",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class))),
    @ApiResponse(responseCode = "404", description = "Enterprise not found with the specified ID",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class)))
  })
  public ResponseEntity<Optional<EnterpriseResponseDTO>> findById(@PathVariable UUID id) {
    return ResponseEntity.ok(enterpriseService.findEntenpriseById(id));
  }

  @GetMapping("/cnpj")
  @Operation(summary = "Find a enterprise by CNPJ", description = "Returns deiled information of a specific enterprise identified by CNPJ")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Enterprise retrieved successfully."),
    @ApiResponse(responseCode = "400", description = "Invalid CNPJ format provided.",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class))),
    @ApiResponse(responseCode = "404", description = "Enterprise not found with the specified CNPJ",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class)))
  })
  public ResponseEntity<Optional<EnterpriseResponseDTO>> findByCnpj(@RequestParam String cnpj) {
    return ResponseEntity.ok(enterpriseService.findEnterpriseByCnpj(cnpj));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Edit or update a enterprise by ID", description = "Updates a enterprise by providing its ID un the URL path and the update data in the request body.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Enterprise updated successfully."),
    @ApiResponse(responseCode = "404", description = "Enterprise not found.",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class))),
    @ApiResponse(responseCode = "422", description = "Validation error when a field is invalid.",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class))),
    @ApiResponse(responseCode = "400", description = "Invalid UUID format provided.",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class)))
  })
  public ResponseEntity<EnterpriseResponseDTO> updateEnterprise(@PathVariable("id") String id, @io.swagger.v3.oas.annotations.parameters.RequestBody(
    description = "Enterprise data to update",
    required = true,
    content = @Content(
      schema = @Schema(implementation = EnterpriseRequestDTO.class),
      examples = {
        @ExampleObject(
          name = "Update Enterprise Example",
          summary = "Example of enterprise update request",
          value = "{\n" +
        "  \"name\": \"Estacionamento Central\",\n" +
        "  \"address\": {\n" +
        "    \"street\": \"Avenida Paulista, 1000\",\n" +
        "    \"city\": \"São Paulo\",\n" +
        "    \"state\": \"SP\",\n" +
        "    \"postalCode\": \"01310-100\"\n" +
        "  },\n" +
        "  \"motorcycleSpaces\": 20,\n" +
        "  \"carSpaces\": 100\n" +
        "}"
        )})) @RequestBody @Valid EnterpriseModel enterprise) {
    enterprise.setId(UUID.fromString(id));
    return ResponseEntity.ok(enterpriseService.updateEnterpriseById(enterprise));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete enterprise by ID", description = "In this endpoin it is possible to delete a enterprise, jus by informing the ID in the URL.")
    @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Enterprise delete successfully."),
    @ApiResponse(responseCode = "404", description = "Enterprise not found.",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class))),
    @ApiResponse(responseCode = "400", description = "Invalid UUID format provided.",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class))),
    @ApiResponse(responseCode = "400", description = "It is not allowed to delete a company that has parked vehicles",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class)))
  })
  public ResponseEntity<Void> deleteEnteprise(@PathVariable UUID id) {
    enterpriseService.deleteEnterpriseById(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}/occupation-report")
  @Operation(summary = "Shows a report on vacancies and occupations by enterprise ID", 
            description = "Lists the number of parked motorcycles, parked cars, total number of spaces, occupied and empy spaces")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Report retrieved successfully"),
    @ApiResponse(responseCode = "404", description = "Enterprise not found.",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class))),
    @ApiResponse(responseCode = "400", description = "Invalid UUID format provided.",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class))),
  })
  public ResponseEntity<ParkingReportDTO> occupationReport(@PathVariable UUID id) {
    return ResponseEntity.ok(enterpriseService.generateOccupancyReport(id));
  }

  @GetMapping("/{id}/movement-report")
  @Operation(summary = "List all veichles that passed through a enterprise, by enterprise ID", 
             description = "Retrieved all movements active and finished at a enterprise.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Report retrieved successfully"),
    @ApiResponse(responseCode = "404", description = "Enterprise not found.",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class))),
    @ApiResponse(responseCode = "400", description = "Invalid UUID format provided.",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class))),
  })
  public ResponseEntity<List<VehicleReportDTO>> movementReport(@PathVariable UUID id) {
    List<VehicleReportDTO> report = enterpriseService.generateMovementReport(id);
    return ResponseEntity.ok(report);
  }

  @GetMapping("/{id}/report-parked")
  @Operation(summary = "List all parked vehicles at enterprise", 
             description = "Retrieved all vehicles parked at enterprise, by enterprise ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Report retrieved successfully"),
    @ApiResponse(responseCode = "404", description = "Enterprise not found.",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class))),
    @ApiResponse(responseCode = "400", description = "Invalid UUID format provided.",
      content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = ResponseError.class))),
  })
  public ResponseEntity<List<ActiveVehicleDTO>> parkedReport(@PathVariable UUID id) {
    List<ActiveVehicleDTO> report = enterpriseService.listActiveVehicles(id);
    return ResponseEntity.ok(report);
  }
}
