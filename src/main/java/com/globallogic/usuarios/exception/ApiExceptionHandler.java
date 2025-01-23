package com.globallogic.usuarios.exception;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.globallogic.usuarios.model.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {

  @ExceptionHandler(UserExistsException.class)
  public ResponseEntity<ErrorResponse> handleUserExistsException(UserExistsException userExistsException) {
    ErrorResponse errorResponse = ErrorResponse.builder()
        .error(com.globallogic.usuarios.model.ErrorResponse.Error.builder()
            .codigo(HttpStatus.CONFLICT.value())
            .detail(userExistsException.getMessage())
            .timestamp(Timestamp.valueOf(LocalDateTime.now()))
            .build())
        .build();
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  protected ResponseEntity<Object> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    List<String> details = new ArrayList<>();
    Map<String, List<FieldError>> fieldErrors = ex.getFieldErrors().stream().filter(Objects::nonNull)
        .collect(Collectors.groupingBy(FieldError::getField));
    fieldErrors.forEach((field, errors) -> {
      details.add(field + ": " + String.join(",", errors.stream().map(FieldError::getDefaultMessage).toList()));
    });
    ErrorResponse errorResponse = ErrorResponse.builder()
        .error(com.globallogic.usuarios.model.ErrorResponse.Error.builder()
            .codigo(HttpStatus.BAD_REQUEST.value())
            .detail(details.toString())
            .timestamp(Timestamp.valueOf(LocalDateTime.now()))
            .build())
        .build();
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = HttpMessageNotReadableException.class)
  protected ResponseEntity<Object> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex) {
    ErrorResponse errorResponse = null;
    if (ex.getCause() instanceof UnrecognizedPropertyException exception) {
      errorResponse = ErrorResponse.builder()
          .error(com.globallogic.usuarios.model.ErrorResponse.Error.builder()
              .codigo(HttpStatus.BAD_REQUEST.value())
              .detail("Unknown Field: " + exception.getPropertyName())
              .timestamp(Timestamp.valueOf(LocalDateTime.now()))
              .build())
          .build();
    }
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UserDoesNotExistException.class)
  public ResponseEntity<ErrorResponse> handleUserDoesNotExistException(
      UserDoesNotExistException userDoesNotExistException) {
    ErrorResponse errorResponse = ErrorResponse.builder()
        .error(com.globallogic.usuarios.model.ErrorResponse.Error.builder()
            .codigo(HttpStatus.NOT_FOUND.value())
            .detail(userDoesNotExistException.getMessage())
            .timestamp(Timestamp.valueOf(LocalDateTime.now()))
            .build())
        .build();
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidTokenException.class)
  public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException invalidTokenException) {
    ErrorResponse errorResponse = ErrorResponse.builder()
        .error(com.globallogic.usuarios.model.ErrorResponse.Error.builder()
            .codigo(HttpStatus.FORBIDDEN.value())
            .detail(invalidTokenException.getMessage())
            .timestamp(Timestamp.valueOf(LocalDateTime.now()))
            .build())
        .build();
    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
  }

}
