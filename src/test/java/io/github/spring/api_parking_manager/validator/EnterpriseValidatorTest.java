package io.github.spring.api_parking_manager.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.github.spring.api_parking_manager.exception.DuplicateRecordException;
import io.github.spring.api_parking_manager.model.EnterpriseModel;
import io.github.spring.api_parking_manager.repository.EnterpriseRepository;

public class EnterpriseValidatorTest {
  
  @Mock
  private EnterpriseRepository enterpriseRepository;

  private EnterpriseValidator validator;

  @BeforeEach
  void config() {
    MockitoAnnotations.openMocks(this);
    validator = new EnterpriseValidator(enterpriseRepository);
  }

  @Test
  @DisplayName("Should throw exception when CNPJ already exists for a new enterprise")
  public void testThrowExceptionWhenCnpjAlreadyExistisForNewEnterprise() {
    UUID id = UUID.fromString("7104bb59-2e2e-451e-abaa-a9c63910a1bf");
    EnterpriseModel enterprise = new EnterpriseModel();
    enterprise.setId(null);
    enterprise.setCnpj("62.835.628/0001-64");

    EnterpriseModel existingEnterprise = new EnterpriseModel();
    existingEnterprise.setId(id);
    existingEnterprise.setCnpj("62.835.628/0001-64");

    when(enterpriseRepository.findByCnpj("62.835.628/0001-64"))
              .thenReturn(Optional.of(existingEnterprise));

    DuplicateRecordException except = assertThrows(
                  DuplicateRecordException.class,
                  () -> validator.validate(enterprise));
    
    assertEquals("This CNPJ already exists!", except.getMessage());
    verify(enterpriseRepository, times(1)).findByCnpj("62.835.628/0001-64");
  }

  @Test
  @DisplayName("Should not throw an exception when the CNPJ does not exist")
  public void testNotThrowsExceptionWhenCnpjDoesNotExist() {
    EnterpriseModel enterprise = new EnterpriseModel();
    enterprise.setId(null);
    enterprise.setCnpj("62.835.628/0001-64");

    when(enterpriseRepository.findByCnpj("62.835.628/0001-64")).thenReturn(Optional.empty());

    assertDoesNotThrow(() -> validator.validate(enterprise));
  }

  @Test
  @DisplayName("Should throw an exception when the CNPJ belongs to another enterprise")
  public void testThrowExceptionWhenCnpjBelogsToDifferentEnterprise() {
    UUID id = UUID.fromString("7104bb59-2e2e-451e-abaa-a9c63910a1bf");
    UUID id2 = UUID.fromString("601e7093-8c10-40b8-809a-1c992a234b54");
    
    EnterpriseModel enterprise = new EnterpriseModel();
    enterprise.setId(id);
    enterprise.setCnpj("62.835.628/0001-64");

    EnterpriseModel existingEnterprise = new EnterpriseModel();
    existingEnterprise.setId(id2);
    existingEnterprise.setCnpj("62.835.628/0001-64");

    when(enterpriseRepository.findByCnpj("62.835.628/0001-64")).thenReturn(Optional.of(existingEnterprise));

    assertThrows(DuplicateRecordException.class, () -> validator.validate(enterprise));
  }

}
