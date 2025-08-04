package io.github.spring.api_parking_manager.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import io.github.spring.api_parking_manager.model.AddressModel;
import io.github.spring.api_parking_manager.model.EnterpriseModel;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
public class EnterpriseRepositoryTest {
  
  @Autowired
  private TestEntityManager testEntityManager;
  
  @Autowired
  private EnterpriseRepository enterpriseRepository;

  private EnterpriseModel mockEnterprise;
  private AddressModel mockAddress;

  @BeforeEach
  void config() {
    mockAddress = new AddressModel();
    mockAddress.setStreet("Avenida Paulista");
    mockAddress.setCity("São Paulo");
    mockAddress.setState("SP");
    mockAddress.setPostalCode("01310-100");

    mockEnterprise = new EnterpriseModel();
    mockEnterprise.setName("Estaciona Fácil");
    mockEnterprise.setCnpj("63.408.531/0001-38");
    mockEnterprise.setAddress(mockAddress);
    mockEnterprise.setMotorcycleSpaces(10);
    mockEnterprise.setCarSpaces(25);
  }

  @Test
  @DisplayName("Should find enterprise by CNPJ")
  public void testFindEnterpriseByCnpj() {
    mockEnterprise = testEntityManager.persistAndFlush(mockEnterprise);

    Optional<EnterpriseModel> enterpriseFinded = enterpriseRepository.findByCnpj(mockEnterprise.getCnpj());

    assertTrue(enterpriseFinded.isPresent(), "Enterprise should be present");

    EnterpriseModel foundEnterprise = enterpriseFinded.get();
    assertEquals(mockEnterprise.getId(), foundEnterprise.getId());
    assertEquals(mockEnterprise.getName(), foundEnterprise.getName());
    assertEquals(mockEnterprise.getCnpj(), foundEnterprise.getCnpj());
    assertEquals(mockEnterprise.getAddress(), foundEnterprise.getAddress());
    assertEquals(mockEnterprise.getMotorcycleSpaces(), foundEnterprise.getMotorcycleSpaces());
    assertEquals(mockEnterprise.getCarSpaces(), foundEnterprise.getCarSpaces());
  }
}
