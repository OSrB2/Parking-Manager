package io.github.spring.api_parking_manager.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.spring.api_parking_manager.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, UUID>{
  
  Optional<UserModel> findByEmail(String email);

  Optional<UserModel> findByCpf(String cpf);

  List<UserModel> findByName(String name);
}
