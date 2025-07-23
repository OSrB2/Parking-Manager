package io.github.spring.api_parking_manager.validator;

import java.util.Optional;

import org.springframework.stereotype.Component;

import io.github.spring.api_parking_manager.exception.DuplicateRecordException;
import io.github.spring.api_parking_manager.model.EnterpriseModel;
import io.github.spring.api_parking_manager.repository.EnterpriseRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EnterpriseValidator {
  
  private final EnterpriseRepository enterpriseRepository;

  public void validate(EnterpriseModel enterprise) {
    if (cnpjExists(enterprise)) {
      throw new DuplicateRecordException("This CNPJ already exists!");
    }
  }

  private boolean cnpjExists(EnterpriseModel enterprise) {
    Optional<EnterpriseModel> byCnpj = enterpriseRepository.findByCnpj(enterprise.getCnpj());

    if (enterprise.getId() == null) {
      return byCnpj.isPresent();
    }
    return byCnpj
      .map(EnterpriseModel::getId)
      .stream()
      .anyMatch(id -> !id.equals(enterprise.getId()));
  }
}
