package io.github.spring.api_parking_manager.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.spring.api_parking_manager.exception.EntityNotFoundException;
import io.github.spring.api_parking_manager.exception.ResponseError;

@RestControllerAdvice
public class GlobalExceptionHandler {
  
  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseError handleEntityNotFoundExcepion(EntityNotFoundException e) {
    return ResponseError.responseDefault(e.getMessage());
  }
}
