package io.github.spring.api_parking_manager.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.spring.api_parking_manager.model.UserModel;

@SpringBootTest
public class TokenServiceTest {
  
  @Autowired
  private TokenService tokenService;

  private UserModel mockUser;

  @BeforeEach
  void config() {
    mockUser = new UserModel();
    mockUser.setLogin("user@login.test");
    mockUser.setPassword("password1234");
  }

  @Test
  @DisplayName("Should generate and validate token")
  public void testGenerateAndValidateToken() {
    String token = tokenService.generateToken(mockUser);

    assertNotNull(token, "Token should not be null");
    assertFalse(token.isEmpty(), "Token should not be empty"); 
  }

  @Test
  @DisplayName("Should validate a valid token")
  public void testValidateTokenValid() {
    String token = tokenService.generateToken(mockUser);
    String loginFromToken = tokenService.validateToken(token);

    assertEquals(mockUser.getLogin(), loginFromToken, "Login shoud match");
  }

  @Test
  @DisplayName("Should return empty string for invalid token")
  public void testValidateTokenInvalid() {
    String invalidLogin = tokenService.validateToken("imNotAToken");

    assertEquals("", invalidLogin, "Invalid token should return empty string");
  }
}
