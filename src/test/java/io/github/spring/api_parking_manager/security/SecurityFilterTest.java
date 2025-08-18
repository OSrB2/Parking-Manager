package io.github.spring.api_parking_manager.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import io.github.spring.api_parking_manager.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SpringBootTest
public class SecurityFilterTest {

  @Autowired
  private SecurityFilter securityFilter;
  
  @MockBean
  private TokenService tokenService;

  @MockBean
  private UserRepository userRepository;

  private HttpServletRequest request;
  private HttpServletResponse response;
  private FilterChain filterChain;

  @BeforeEach
  void config() {
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
    filterChain = mock(FilterChain.class);
    SecurityContextHolder.clearContext();
  }

    @AfterEach
    void tearDown() {
      SecurityContextHolder.clearContext();
    }


  @Test
  @DisplayName("Should not authenticate if token is invalid")
  public void testInvalidToken() throws Exception {
    when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");
    when(tokenService.validateToken("invalid-token")).thenReturn("");

    UserDetails mockUser = mock(UserDetails.class);
    
    when(mockUser.getAuthorities()).thenReturn(Collections.emptyList());
    when(userRepository.findByLogin("")).thenReturn(mockUser);

    securityFilter.doFilter(request, response, filterChain);

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    assertNotNull(auth, "Authentication should have been created");
    assertTrue(auth.getAuthorities().isEmpty(), "Authorities should be empty");
    verify(filterChain).doFilter(request, response);
}

  @Test
  @DisplayName("Should not authenticate if no token is present")
  public void testNoToken() throws Exception {
    when(request.getHeader("Authorization")).thenReturn(null);

    securityFilter.doFilterInternal(request, response, filterChain);

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    assertNull(auth, "Authentiation should not be set");
    verify(filterChain).doFilter(request, response);
  }

  @Test
  @DisplayName("Should not authenticate if user is not found is repository")
  public void testUserNotFound() throws Exception {
    when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
    when(tokenService.validateToken("valid-token")).thenReturn("user@login.test");

    UserDetails mockUser = mock(UserDetails.class);
    when(mockUser.getAuthorities()).thenReturn(Collections.emptyList());
    when(userRepository.findByLogin("user@login.test")).thenReturn(mockUser);

    securityFilter.doFilterInternal(request, response, filterChain);

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    assertNotNull(auth, "Authentication should have been created");
    assertTrue(auth.getAuthorities().isEmpty(), "Authorities should be empty");

    verify(filterChain).doFilter(request, response);
  }
}
