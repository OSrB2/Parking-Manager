package io.github.spring.api_parking_manager.exception;

public class UnsupportedVehicleTypeException extends RuntimeException{
  public UnsupportedVehicleTypeException(String message) {
    super(message);
  }
}
