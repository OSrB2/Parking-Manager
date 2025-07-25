package io.github.spring.api_parking_manager.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {
  
  private final VehicleService vehicleService;
  private final VehicleMapper vehicleMapper;

  @PostMapping
  public ResponseEntity<VehicleResponseDTO> registerVehicle(@RequestBody @Valid VehicleRequestDTO vehicleRequestDTO) {
    VehicleModel vehicle = vehicleMapper.toEntity(vehicleRequestDTO);
    return ResponseEntity.ok(vehicleService.register(vehicle));
  }

  @GetMapping
  public ResponseEntity<List<VehicleResponseDTO>> listAll() {
    return ResponseEntity.ok(vehicleService.listAllVehicles());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Optional<VehicleResponseDTO>> findById(@PathVariable UUID id) {
    return ResponseEntity.ok(vehicleService.finalVehicleById(id));
  }

  @GetMapping("/plate")
  public ResponseEntity<Optional<VehicleResponseDTO>> findByPlate(@RequestParam String plate) {
    return ResponseEntity.ok(vehicleService.findVehicleByPlate(plate));
  }

  @PutMapping("/{id}")
  public ResponseEntity<VehicleResponseDTO> updateVehicle(@PathVariable("id") String id,
                                                    @RequestBody @Valid VehicleModel vehicle) {
    vehicle.setId(UUID.fromString(id));
    return ResponseEntity.ok(vehicleService.updateVehicleById(vehicle));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteVehicle(@PathVariable UUID id) {
    vehicleService.deleteVehicleByID(id);
    return ResponseEntity.noContent().build();
  }
}
