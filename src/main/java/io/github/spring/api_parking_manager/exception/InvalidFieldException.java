package io.github.spring.api_parking_manager.exception;

import lombok.Getter;

public class InvalidFieldException extends RuntimeException {

  @Getter
  private String field;

  public InvalidFieldException(String field, String message) {
    super(message);
    this.field = field;
  }
  
}
