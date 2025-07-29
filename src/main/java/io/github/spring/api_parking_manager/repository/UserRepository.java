package io.github.spring.api_parking_manager.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import io.github.spring.api_parking_manager.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, UUID>{
  UserDetails findByLogin(String email);

  Optional<UserModel> findByEmail(String email);

  Optional<UserModel> findByCpf(String cpf);

  List<UserModel> findByName(String name);
}
