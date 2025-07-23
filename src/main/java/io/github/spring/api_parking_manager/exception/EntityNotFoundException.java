package io.github.spring.api_parking_manager.exception;

public class EntityNotFoundException extends RuntimeException{
  public EntityNotFoundException(String message) {
    super(message);
  }
}
