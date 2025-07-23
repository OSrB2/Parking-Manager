package io.github.spring.api_parking_manager.exception;

public class DuplicateRecordException extends RuntimeException {
  public DuplicateRecordException(String message) {
    super(message);
  }
}
