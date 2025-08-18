package io.github.spring.api_parking_manager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.spring.api_parking_manager.model.UserModel;
import io.github.spring.api_parking_manager.model.dtos.AuthenticationDTO;
import io.github.spring.api_parking_manager.repository.UserRepository;
import io.github.spring.api_parking_manager.security.TokenService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {
  
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private TokenService tokenService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private AuthenticationManager authenticationManager;

  @WithMockUser(username = "user@login.test", password = "123456")
  @Test
  @DisplayName("Should return a sucessfull login")
  public void testSuccessfullLogin() throws Exception {

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(new UsernamePasswordAuthenticationToken(new UserModel(), null));

    mockMvc.perform(post("/api/auth/login")
                  .content("{\"login\":\"user@login.test\", \"password\":\"123456\"}").with(csrf())
                  .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk());
  }

  @WithMockUser(username = "user@login.test", password = "123456")
  @Test
  @DisplayName("Should register a new user")
  public void testRegisterNewUser() throws Exception {
    AuthenticationDTO data = new AuthenticationDTO("user@login.test", "123456");
    String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

    when(userRepository.findByLogin(any())).thenReturn(null);

    mockMvc.perform(post("/api/auth/register")
           .with(csrf())
           .contentType(MediaType.APPLICATION_JSON)
           .content(objectMapper.writeValueAsString(data)))
      .andExpect(status().isOk())
      .andExpect(content().string("User registered successfully!!"));

    verify(userRepository, times(1)).findByLogin("user@login.test");
    verify(userRepository, times(1)).save(any(UserModel.class));
  }

  @WithMockUser(username = "user@login.test", password = "123456")
  @Test
  @DisplayName("Should return message if user already exists")
  public void testRegisterUserAlreadyExists() throws Exception {
    AuthenticationDTO data = new AuthenticationDTO("userExist", "passwordUserExist");

    when(userRepository.findByLogin("userExist")).thenReturn(new UserModel("userExist", "encryptedPassword"));

    mockMvc.perform(post("/api/auth/register")
           .with(csrf())
           .contentType(MediaType.APPLICATION_JSON)
           .content(objectMapper.writeValueAsString(data)))
      .andExpect(status().isBadRequest())
      .andExpect(content().string("This user already exists!"));
  }
}
