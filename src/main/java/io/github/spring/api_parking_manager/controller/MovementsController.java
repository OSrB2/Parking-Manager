package io.github.spring.api_parking_manager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.spring.api_parking_manager.model.MovementsModel;
import io.github.spring.api_parking_manager.service.MovementsService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/movements")
@RequiredArgsConstructor
public class MovementsController {
  
  private final MovementsService movementsService;

  @PostMapping
  public ResponseEntity<MovementsModel> register(@RequestBody MovementsModel movement) {
    MovementsModel movementsModel = movementsService.registerEntry(movement.getVehicle().getId(), movement.getEnterprise().getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(movementsModel);
  }

  @GetMapping
  public ResponseEntity<List<MovementsModel>> list() {
    return ResponseEntity.ok(movementsService.listAllMovements());
  }
}
