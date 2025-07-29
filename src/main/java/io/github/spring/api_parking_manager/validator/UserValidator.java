package io.github.spring.api_parking_manager.validator;

import java.util.Optional;

import org.springframework.stereotype.Component;

import io.github.spring.api_parking_manager.exception.DuplicateRecordException;
import io.github.spring.api_parking_manager.model.UserModel;
import io.github.spring.api_parking_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserValidator {  

  private final UserRepository userRepository;
  
  public void validate(UserModel user) {
    if (cpfExists(user)) {
      throw new DuplicateRecordException("This CPF already exists!");
    }
  }
  
  private boolean cpfExists(UserModel user) {
    Optional<UserModel> byCpf = userRepository.findByCpf(user.getCpf());

    if (user.getId() == null) {
      return byCpf.isPresent();
    }

    return byCpf
      .map(UserModel::getId)
      .stream()
      .anyMatch(id -> !id.equals(user.getId()));
  }
}
