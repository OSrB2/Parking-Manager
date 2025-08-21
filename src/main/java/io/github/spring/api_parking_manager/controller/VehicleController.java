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

import io.github.spring.api_parking_manager.model.VehicleModel;
import io.github.spring.api_parking_manager.model.dtos.VehicleRequestDTO;
import io.github.spring.api_parking_manager.model.dtos.VehicleResponseDTO;
import io.github.spring.api_parking_manager.model.mappers.VehicleMapper;
import io.github.spring.api_parking_manager.service.VehicleService;
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
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicles")
public class VehicleController {
  
  private final VehicleService vehicleService;
  private final VehicleMapper vehicleMapper;

  @PostMapping
  @Operation(summary = "Register a new vehicle", description = "Create and register a new vehicle in the system.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Vehicle successfully registered.",
      content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = VehicleResponseDTO.class)
    )),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "409", description = "Conflict"),
    @ApiResponse(responseCode = "422", description = "Validation error")
  })
  public ResponseEntity<VehicleResponseDTO> registerVehicle(@RequestBody @Valid VehicleRequestDTO vehicleRequestDTO) {
    VehicleModel vehicle = vehicleMapper.toEntity(vehicleRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.register(vehicle));
  }

  @GetMapping
  @Operation(summary = "List all vehicles", description = "Retrieve a list of all vehicles currently registered in the system.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "List of vehicles successfully retrieved.",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = VehicleResponseDTO.class)
    )),
    @ApiResponse(responseCode = "400", description = "Bad request"),
    @ApiResponse(responseCode = "404", description = "Not found"),
    @ApiResponse(responseCode = "422", description = "Validation error")
  })
  public ResponseEntity<List<VehicleResponseDTO>> listAll() {
    return ResponseEntity.ok(vehicleService.listAllVehicles());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Find a vehicle by ID", description = "This endpoint return a vehicle searched by the ID passed in the url.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Return a json with vehicle with ID."),
    @ApiResponse(responseCode = "400", description = "Return a exception when search is not possible."),
    @ApiResponse(responseCode = "404", description = "Return a error messagem when vehicle is not found.")
  })
  public ResponseEntity<Optional<VehicleResponseDTO>> findById(@PathVariable UUID id) {
    return ResponseEntity.ok(vehicleService.findVehicleById(id));
  }

  @GetMapping("/plate")
  @Operation(summary = "Find a vehicle by Plate", description = "This endpoint return a vehicle searched by the plate passed in the params.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Return a json with vehicle with plate."),
    @ApiResponse(responseCode = "400", description = "Return a message when search is not possible."),
    @ApiResponse(responseCode = "404", description = "Return a message when vehicle is not found.")
  })
  public ResponseEntity<Optional<VehicleResponseDTO>> findByPlate(@RequestParam String plate) {
    return ResponseEntity.ok(vehicleService.findVehicleByPlate(plate));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Edit or update a vehicle by ID", description = "Updates a vehicle by providing its ID in the URL path and the updated data in the request body.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Vehicle updated successfully."),
    @ApiResponse(responseCode = "404", description = "Vehicle not found."),
    @ApiResponse(responseCode = "422", description = "Validation error when a field is invalid."),
    @ApiResponse(responseCode = "400", description = "Invalid UUID format provided.")
  })
  public ResponseEntity<VehicleResponseDTO> updateVehicle(@PathVariable("id") String id,@io.swagger.v3.oas.annotations.parameters.RequestBody(
    description = "Vehicle data to update",
    required = true,
    content = @Content(
      schema = @Schema(implementation = VehicleRequestDTO.class),
      examples = {
        @ExampleObject(
          name = "Update Vehicle Example",
          summary = "Example of vehicle update request",
          value = "{\n" +
          "  \"plate\": \"XYZ-5678\",\n" +
          "  \"brand\": \"Honda\",\n" +
          "  \"model\": \"Civic\",\n" +
          "  \"color\": \"Red\",\n" +
          "  \"type\": \"CAR\"\n" +
          "}"
        )})) @RequestBody @Valid VehicleModel vehicle) {
    vehicle.setId(UUID.fromString(id));
    return ResponseEntity.ok(vehicleService.updateVehicleById(vehicle));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete vehicle by ID", description = "In this endpoint it is possible to delete a vehicle, jus by informing the ID in the URL.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Vehicle delete successfully."),
    @ApiResponse(responseCode = "404", description = "Vehicle not found."),
    @ApiResponse(responseCode = "400", description = "It is not allowed to delete a vehicle that is parked")
  })
  public ResponseEntity<Void> deleteVehicle(@PathVariable UUID id) {
    vehicleService.deleteVehicleByID(id);
    return ResponseEntity.noContent().build();
  }
}
