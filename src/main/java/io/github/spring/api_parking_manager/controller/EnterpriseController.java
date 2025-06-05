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
import org.springframework.web.bind.annotation.RestController;

import io.github.spring.api_parking_manager.model.EnterpriseModel;
import io.github.spring.api_parking_manager.service.EnterpriseService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/enterprises")
@RequiredArgsConstructor
public class EnterpriseController {
  
  private final EnterpriseService enterpriseService;

  @PostMapping
  public ResponseEntity<EnterpriseModel> registerEnterprise(@RequestBody EnterpriseModel enterpriseModel) {
    EnterpriseModel enterprise = enterpriseService.register(enterpriseModel);
    return ResponseEntity.ok().body(enterprise);
  }

  @GetMapping
  public ResponseEntity<List<EnterpriseModel>> listAll() {
    return ResponseEntity.ok(enterpriseService.listAllEnterprises());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Optional<EnterpriseModel>> findById(@PathVariable UUID id) {
    return ResponseEntity.ok(enterpriseService.findEntenpriseById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<EnterpriseModel> updateEnterprise(@PathVariable("id") String id, 
                                                          @RequestBody EnterpriseModel enterprise) {
    enterprise.setId(UUID.fromString(id));
    return ResponseEntity.ok(enterpriseService.updateEnterpriseById(enterprise));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEnteprise(@PathVariable UUID id) {
    enterpriseService.deleteEnterpriseById(id);
    return ResponseEntity.noContent().build();
  }
}
