package io.github.spring.api_parking_manager.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import io.github.spring.api_parking_manager.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, UUID>{

  UserDetails findByLogin(String login);
}
