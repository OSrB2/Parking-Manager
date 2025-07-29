package io.github.spring.api_parking_manager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.spring.api_parking_manager.model.UserModel;
import io.github.spring.api_parking_manager.model.dtos.UserRequestDTO;
import io.github.spring.api_parking_manager.model.dtos.UserResponseDTO;
import io.github.spring.api_parking_manager.model.mappers.UserMapper;
import io.github.spring.api_parking_manager.repository.UserRepository;
import io.github.spring.api_parking_manager.validator.UserValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
  
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final UserValidator validator;

  public UserResponseDTO register(UserModel user) {
    validator.validate(user);
    UserModel savedUser = userRepository.save(user);
    return userMapper.tResponseDTO(savedUser);
  }

  public List<UserResponseDTO> listAllUsers() {
    List<UserModel> users = userRepository.findAll();

    if (users.isEmpty()) {
      throw new EntityNotFoundException("Users not found!");
    }

    List<UserResponseDTO> userDTO = new ArrayList<>();

    for (UserModel user : users) {
      userDTO.add(userMapper.tResponseDTO(user));
    }
    return userDTO;
  }

  public Optional<UserResponseDTO> findUserById(UUID id) {
    Optional<UserModel> userOptional = userRepository.findById(id);

    if (userOptional.isEmpty()) {
      throw new EntityNotFoundException("User not found!");
    }

    return userRepository.findById(id)
      .map(userMapper::tResponseDTO);
  }

  public Optional<UserResponseDTO> findUserByEmail(String email) {
    Optional<UserModel> userOptional = userRepository.findByEmail(email);

    if (userOptional.isEmpty()) {
      throw new EntityNotFoundException("User not found!");
    }

    return userRepository.findByEmail(email)
      .map(userMapper::tResponseDTO);
  }

  public Optional<UserResponseDTO> findUserByCpf(String cpf) {
    Optional<UserModel> userOptional = userRepository.findByCpf(cpf);

    if (userOptional.isEmpty()) {
      throw new EntityNotFoundException("User not found!");
    }

    return userRepository.findByCpf(cpf)
      .map(userMapper::tResponseDTO);
  }

  public List<UserResponseDTO> findUserByName(String name) {
    List<UserModel> listUsers = userRepository.findByName(name);

    if (listUsers.isEmpty()) {
      throw new EntityNotFoundException("Users not found!");
    }
    
    List<UserResponseDTO> userDTO = new ArrayList<>();

    for (UserModel user : listUsers) {
      userDTO.add(userMapper.tResponseDTO(user));
    }
    return userDTO;
  }

}
