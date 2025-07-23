package io.github.spring.api_parking_manager.handlers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.spring.api_parking_manager.exception.DuplicateRecordException;
import io.github.spring.api_parking_manager.exception.EntityNotFoundException;
import io.github.spring.api_parking_manager.exception.FieldErrorImpl;
import io.github.spring.api_parking_manager.exception.InvalidFieldException;
import io.github.spring.api_parking_manager.exception.NoSpotsAvailableException;
import io.github.spring.api_parking_manager.exception.OperationNotPermittedException;
import io.github.spring.api_parking_manager.exception.ResponseError;
import io.github.spring.api_parking_manager.exception.UnsupportedVehicleTypeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ResponseError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    List<FieldError> fieldErrors = e.getFieldErrors();

    List<FieldErrorImpl> fieldErrorList = fieldErrors
      .stream()
      .map(fe -> new FieldErrorImpl(fe.getField(), fe.getDefaultMessage()))
      .collect(Collectors.toList());
    return new ResponseError(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Validation error", fieldErrorList);
  }
  
  @ExceptionHandler(DuplicateRecordException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseError handleDuplicateRecordException(DuplicateRecordException e) {
    return ResponseError.conflict(e.getMessage());
  }

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

  @ExceptionHandler(InvalidFieldException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ResponseError handleInvalidFieldException(InvalidFieldException e) {
    return new ResponseError(HttpStatus.UNPROCESSABLE_ENTITY.value(),
    "Validation error",
    List.of(new FieldErrorImpl(e.getField(), e.getMessage())));
  }

  
}
