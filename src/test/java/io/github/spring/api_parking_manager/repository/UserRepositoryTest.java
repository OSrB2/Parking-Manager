package io.github.spring.api_parking_manager.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import io.github.spring.api_parking_manager.model.UserModel;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
public class UserRepositoryTest {
  
  @Autowired
  private TestEntityManager testEntityManager;

  @Autowired
  private UserRepository userRepository;

  private UserModel mockUser;
  @BeforeEach
  void config() {
    mockUser = new UserModel();
    mockUser.setLogin("user@login.test");
    mockUser.setPassword("123456");
  }

  @Test
  @DisplayName("Should find user by login")
  public void testFindUSerByLogin() {
    mockUser = testEntityManager.persistAndFlush(mockUser);

    UserDetails userFound = userRepository.findByLogin(mockUser.getLogin());

    assertNotNull(userFound, "User should be found");
    assertEquals(mockUser.getLogin(), userFound.getUsername(), "Login should match");
    assertEquals(mockUser.getAuthorities(), userFound.getAuthorities(), "Authorities should match");
    assertTrue(userFound.isEnabled(), "User should be enabled");
  }
}
