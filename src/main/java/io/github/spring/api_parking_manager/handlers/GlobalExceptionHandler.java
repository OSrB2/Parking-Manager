package io.github.spring.api_parking_manager.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.spring.api_parking_manager.exception.EntityNotFoundException;
import io.github.spring.api_parking_manager.exception.NoSpotsAvailableException;
import io.github.spring.api_parking_manager.exception.OperationNotPermittedException;
import io.github.spring.api_parking_manager.exception.ResponseError;
import io.github.spring.api_parking_manager.exception.UnsupportedVehicleTypeException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  
  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseError handleEntityNotFoundExcepion(EntityNotFoundException e) {
    return ResponseError.responseDefault(e.getMessage());
  }

  @ExceptionHandler(NoSpotsAvailableException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseError handleNoSpotsAvailableException(NoSpotsAvailableException e) {
    return ResponseError.responseDefault(e.getMessage());
  }

  @ExceptionHandler(UnsupportedVehicleTypeException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseError handleUnsupportedVehicleType(UnsupportedVehicleTypeException e) {
    return ResponseError.responseDefault(e.getMessage());
  }

  @ExceptionHandler(OperationNotPermittedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseError handleOperationNotPermittedException(OperationNotPermittedException e) {
    return ResponseError.responseDefault(e.getMessage());
  }
}
