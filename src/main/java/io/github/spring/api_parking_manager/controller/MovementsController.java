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
import org.springframework.web.bind.annotation.RestController;

import io.github.spring.api_parking_manager.model.MovementsModel;
import io.github.spring.api_parking_manager.model.dtos.MovementsRequestDTO;
import io.github.spring.api_parking_manager.model.dtos.MovementsResponseDTO;
import io.github.spring.api_parking_manager.service.MovementsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/movements")
@RequiredArgsConstructor
public class MovementsController {
  
  private final MovementsService movementsService;
  @PostMapping
  public ResponseEntity<MovementsResponseDTO> register(@RequestBody @Valid MovementsRequestDTO requestDTO) {
    MovementsResponseDTO response = movementsService.registerEntry(
        requestDTO.vehicleId(),
        requestDTO.enterpriseId()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}

  @GetMapping
  public ResponseEntity<List<MovementsResponseDTO>> listAll() {
    return ResponseEntity.ok(movementsService.listAllMovements());
  }

  @GetMapping("/active")
  public ResponseEntity<List<MovementsResponseDTO>> listAllActive() {
    return ResponseEntity.ok(movementsService.listAllActiveMovements());
  }

  @GetMapping("/finished")
  public ResponseEntity<List<MovementsResponseDTO>> listAllFinished() {
    return ResponseEntity.ok(movementsService.listAllFinishedMovements());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Optional<MovementsResponseDTO>> findById(@PathVariable UUID id) {
    return ResponseEntity.ok(movementsService.findMovementById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<MovementsResponseDTO> existRegister(@PathVariable UUID id) {
    return ResponseEntity.ok(movementsService.registerExit(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMovements(@PathVariable UUID id) {
    movementsService.deleteMovementById(id);
    return ResponseEntity.noContent().build();
  }
}
