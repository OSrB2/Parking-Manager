package io.github.spring.api_parking_manager.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

public record ResponseError(int status, String message, List<FieldErrorImpl> Errors){

  public static ResponseError responseDefault(String message) {
    return new ResponseError(HttpStatus.NOT_FOUND.value(), message, null);
  }
}
