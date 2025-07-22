package io.github.spring.api_parking_manager.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

public record ResponseError(int status, String message, List<FieldErrorImpl> errors){

  public static ResponseError responseDefault(String message) {
    return new ResponseError(HttpStatus.BAD_REQUEST.value(), message, List.of());
  }

  public static ResponseError conflict(String message) {
    return new ResponseError(HttpStatus.CONFLICT.value(), message, List.of());
  }
}
