package io.github.spring.api_parking_manager.exception;

public class NoSpotsAvailableException extends RuntimeException{
  public NoSpotsAvailableException(String message) {
    super(message);
  }
}
