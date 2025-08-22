package io.github.spring.api_parking_manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.spring.api_parking_manager.model.UserModel;
import io.github.spring.api_parking_manager.model.dtos.AuthenticationDTO;
import io.github.spring.api_parking_manager.model.dtos.LoginResponseDTO;
import io.github.spring.api_parking_manager.repository.UserRepository;
import io.github.spring.api_parking_manager.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {
  
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final TokenService tokenService;

  @PostMapping("/register")
  @Operation(summary = "User registration", description = "Registers a new user with username and password. Return 400 if the user already exists.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "User registered successfully."),
    @ApiResponse(responseCode = "401", description = "User already exists.")})
  public ResponseEntity register(@RequestBody @Valid AuthenticationDTO data) {

    if (this.userRepository.findByLogin(data.login()) != null ) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user already exists!");
    }
    
    String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
    UserModel newUser = new UserModel(data.login(), encryptedPassword);

    this.userRepository.save(newUser);

    return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!!");
  }

  @PostMapping("/login")
  @Operation(summary = "User login", description = "User login endpoint. Accepts username and password and returns an error if the user already exists.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "User authenticated successfully, JWT token returned"),
    @ApiResponse(responseCode = "401", description = "Ivalid username or password"),
    @ApiResponse(responseCode = "500", description = "Unexpected server error.")})
  public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
    try {
      var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
      var auth = this.authenticationManager.authenticate(usernamePassword);

      var token = tokenService.generateToken((UserModel) auth.getPrincipal());

      return ResponseEntity.ok(new LoginResponseDTO(token));
    } catch (AuthenticationException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unregistered user");
    }
  }
}
