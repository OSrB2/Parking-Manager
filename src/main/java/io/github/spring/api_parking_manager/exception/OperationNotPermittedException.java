package io.github.spring.api_parking_manager.exception;

public class OperationNotPermittedException extends RuntimeException{
  public OperationNotPermittedException(String message) {
    super(message);
  }
  
}
