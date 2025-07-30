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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.spring.api_parking_manager.model.UserModel;
import io.github.spring.api_parking_manager.model.dtos.UserRequestDTO;
import io.github.spring.api_parking_manager.model.dtos.UserResponseDTO;
import io.github.spring.api_parking_manager.model.mappers.UserMapper;
import io.github.spring.api_parking_manager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  
  private final UserService userService;
  private final UserMapper userMapper;

  @PostMapping
  public ResponseEntity<UserResponseDTO> registerUser(@RequestBody @Valid UserRequestDTO userRequestDTO) {
    UserModel user = userMapper.toEntity(userRequestDTO);
    return ResponseEntity.ok(userService.register(user));
  }

  @GetMapping
  public ResponseEntity<List<UserResponseDTO>> listAll() {
    return ResponseEntity.ok(userService.listAllUsers());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Optional<UserResponseDTO>> findById(@PathVariable UUID id) {
    return ResponseEntity.ok(userService.findUserById(id));
  }

  @GetMapping("/cpf")
  public ResponseEntity<Optional<UserResponseDTO>> findByCpf(@RequestParam String cpf) {
    return ResponseEntity.ok(userService.findUserByCpf(cpf));
  }

  @GetMapping("/email")
  public ResponseEntity<Optional<UserResponseDTO>> findByEmail(@RequestParam String email) {
    return ResponseEntity.ok(userService.findUserByEmail(email));
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDTO> updateUser(@PathVariable("id") String id,
                                                          @RequestBody @Valid UserModel user ) {
    user.setId(UUID.fromString(id));
    return ResponseEntity.ok(userService.updateUserById(user));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
    userService.deleteUserById(id);
    return ResponseEntity.noContent().build();
  }
}
