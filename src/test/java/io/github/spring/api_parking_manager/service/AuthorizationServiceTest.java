package io.github.spring.api_parking_manager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import io.github.spring.api_parking_manager.model.UserModel;
import io.github.spring.api_parking_manager.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceTest {
  
  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private AuthorizationService authorizationService;

  private UserModel mockUser;

  @BeforeEach
  void config() {
    mockUser = new UserModel();
    mockUser.setLogin("user@login.test");
    mockUser.setPassword("123456");
  }

  @Test
  @DisplayName("Should load user by Username(login)")
  public void testLoadUserByUsername() {
    when(userRepository.findByLogin(mockUser.getLogin())).thenReturn(mockUser);

    UserDetails result = authorizationService.loadUserByUsername(mockUser.getLogin());

    assertTrue(result.isEnabled());
    assertEquals(mockUser, result);
  }
}
