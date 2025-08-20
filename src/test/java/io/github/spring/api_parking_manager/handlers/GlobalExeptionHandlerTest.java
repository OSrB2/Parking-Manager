package io.github.spring.api_parking_manager.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.BeanProperty;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import io.github.spring.api_parking_manager.exception.DuplicateRecordException;
import io.github.spring.api_parking_manager.exception.EntityNotFoundException;
import io.github.spring.api_parking_manager.exception.InvalidFieldException;
import io.github.spring.api_parking_manager.exception.NoSpotsAvailableException;
import io.github.spring.api_parking_manager.exception.OperationNotPermittedException;
import io.github.spring.api_parking_manager.exception.ResponseError;

public class GlobalExeptionHandlerTest {
  
  private GlobalExceptionHandler handler;

  @BeforeEach
  void config() {
    handler = new GlobalExceptionHandler();
  }

  @Test
  @DisplayName("Should return a exception when duplicate record")
  public void testHandleDuplicateRecordException() {
    DuplicateRecordException except = new DuplicateRecordException("Duplicated entry");

    ResponseError responseError = handler.handleDuplicateRecordException(except);
    
    assertEquals(HttpStatus.CONFLICT.value(), responseError.status());
    assertEquals("Duplicated entry", responseError.message());
    assertTrue(responseError.errors().isEmpty());
  }

  @Test
  @DisplayName("Should return a exception when method argument not valid")
  public void testHandleMethodArgumentNotValidException() {
    BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "object");
    bindingResult.addError(new FieldError("object", "field", "must not be null"));
    MethodArgumentNotValidException except = new MethodArgumentNotValidException(null, bindingResult);

    ResponseError responseError = handler.handleMethodArgumentNotValidException(except);

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), responseError.status());
    assertEquals(1, responseError.errors().size());
    assertEquals("field", responseError.errors().get(0).field());
    assertEquals("must not be null", responseError.errors().get(0).message());
  }

  @Test
  @DisplayName("Should return a exception when entity not found")
  public void testHandleEntityNotFoundException() {
    EntityNotFoundException except = new EntityNotFoundException("Entity not found");
    ResponseError responseError = handler.handleEntityNotFoundExcepion(except);

    assertEquals(HttpStatus.BAD_REQUEST.value(), responseError.status());
    assertEquals("Entity not found", responseError.message());
  }

  @Test
  @DisplayName("Should return a exception when there are no spots available")
  public void testHandleNoSpotsAvailableException() {
    NoSpotsAvailableException except = new NoSpotsAvailableException("No spots");
    ResponseError responseError = handler.handleNoSpotsAvailableException(except);

    assertEquals(HttpStatus.BAD_REQUEST.value(), responseError.status());
    assertEquals("No spots", responseError.message());
  }

  @Test
  @DisplayName("Should return a exception when trying to perform an operation that is not permitted")
  public void testHandleOperationNotPermittedException() {
    OperationNotPermittedException except = new OperationNotPermittedException("Not permitted");
    ResponseError responseError = handler.handleOperationNotPermittedException(except);

    assertEquals(HttpStatus.BAD_REQUEST.value(), responseError.status());
    assertEquals("Not permitted", responseError.message());
  }

  @Test
  @DisplayName("Should return a exception when a field is invalid ")
  public void testHandleInvalidFieldException() {
    InvalidFieldException except = new InvalidFieldException("field", "Invalid field value");
    ResponseError responseError = handler.handleInvalidFieldException(except);

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), responseError.status());
    assertEquals("Validation error", responseError.message());
    assertEquals(1, responseError.errors().size());
    assertEquals("field", responseError.errors().get(0).field());
    assertEquals("Invalid field value", responseError.errors().get(0).message());
  }
}
