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

import io.github.spring.api_parking_manager.model.EnterpriseModel;
import io.github.spring.api_parking_manager.model.dtos.EnterpriseRequestDTO;
import io.github.spring.api_parking_manager.model.dtos.EnterpriseResponseDTO;
import io.github.spring.api_parking_manager.model.mappers.EnterpriseMapper;
import io.github.spring.api_parking_manager.service.EnterpriseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/enterprises")
@RequiredArgsConstructor
public class EnterpriseController {
  
  private final EnterpriseService enterpriseService;
  private final EnterpriseMapper enterpriseMapper;

  @PostMapping
  public ResponseEntity<EnterpriseResponseDTO> registerEnterprise(@RequestBody @Valid EnterpriseRequestDTO enterpriseRequestDTO) {
    EnterpriseModel enterprise = enterpriseMapper.toEntity(enterpriseRequestDTO);
    return ResponseEntity.ok(enterpriseService.register(enterprise));
  }

  @GetMapping
  public ResponseEntity<List<EnterpriseResponseDTO>> listAll() {
    return ResponseEntity.ok(enterpriseService.listAllEnterprises());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Optional<EnterpriseResponseDTO>> findById(@PathVariable UUID id) {
    return ResponseEntity.ok(enterpriseService.findEntenpriseById(id));
  }

  @GetMapping("/cnpj")
  public ResponseEntity<Optional<EnterpriseResponseDTO>> findByCnpj(@RequestParam String cnpj) {
    return ResponseEntity.ok(enterpriseService.findEnterpriseByCnpj(cnpj));
  }

  @PutMapping("/{id}")
  public ResponseEntity<EnterpriseResponseDTO> updateEnterprise(@PathVariable("id") String id, 
                                                          @RequestBody @Valid EnterpriseModel enterprise) {
    enterprise.setId(UUID.fromString(id));
    return ResponseEntity.ok(enterpriseService.updateEnterpriseById(enterprise));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEnteprise(@PathVariable UUID id) {
    enterpriseService.deleteEnterpriseById(id);
    return ResponseEntity.noContent().build();
  }
}
