package com.globallogic.usuarios.exception;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.globallogic.usuarios.model.ErrorResponse;
import com.globallogic.usuarios.model.UserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ApiExceptionHandlerTest {
  @InjectMocks
  ApiExceptionHandler apiExceptionHandler;

  @Test
  void handleUserExistsException() {
    UserExistsException userExistsException = mock(UserExistsException.class);
    when(userExistsException.getMessage()).thenReturn("User already exists");
    var response = apiExceptionHandler.handleUserExistsException(userExistsException);
    assertThat(response.getBody()).isInstanceOf(ErrorResponse.class);
    ErrorResponse errorResponse = response.getBody();
    assertThat(errorResponse.getError()).isNotNull();
    assertThat(errorResponse.getError().getCodigo()).isEqualTo(HttpStatus.CONFLICT.value());
    assertThat(errorResponse.getError().getDetail()).isEqualTo("User already exists");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
  }

  @Test
  void handleMethodArgumentNotValidException() {
    MethodArgumentNotValidException mockException = mock(MethodArgumentNotValidException.class);
    FieldError fieldError = new FieldError("objectName", "fieldName1", "message1");
    FieldError fieldError1 = new FieldError("objectName1", "fieldName2", "message2");
    FieldError fieldError2 = new FieldError("objectName1", "fieldName3", "message3");

    when(mockException.getFieldErrors()).thenReturn(List.of(fieldError, fieldError1, fieldError2));

    var response = apiExceptionHandler.handleMethodArgumentNotValidException(mockException);
    assertThat(response.getBody()).isInstanceOf(ErrorResponse.class);
    ErrorResponse errorResponse = (ErrorResponse) response.getBody();
    assertThat(errorResponse.getError()).isNotNull();
    assertThat(errorResponse.getError().getCodigo()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getError().getDetail()).contains("fieldName1").contains("fieldName2")
        .contains("fieldName3").contains("message1").contains("message2").contains("message3");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

  }

  @Test
  void handleHttpMessageNotReadableException() {
    HttpMessageNotReadableException mockException = mock(HttpMessageNotReadableException.class);
    UnrecognizedPropertyException unrecognizedPropertyException = new UnrecognizedPropertyException(
        null, "", null, UserRequest.class, "lastName", Collections.emptyList());

    when(mockException.getCause()).thenReturn(unrecognizedPropertyException);

    var response = apiExceptionHandler.handleHttpMessageNotReadableException(mockException);
    assertThat(response.getBody()).isInstanceOf(ErrorResponse.class);
    ErrorResponse errorResponse = (ErrorResponse) response.getBody();
    assertThat(errorResponse.getError()).isNotNull();
    assertThat(errorResponse.getError().getCodigo()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getError().getDetail()).isEqualTo("Unknown Field: lastName");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void handleHttpMessageNotReadableException_notUnrecognizedProperty() {
    HttpMessageNotReadableException mockException = mock(HttpMessageNotReadableException.class);

    when(mockException.getCause()).thenReturn(new Exception());

    var response = apiExceptionHandler.handleHttpMessageNotReadableException(mockException);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void handleUserDoesNotExist() {
    UserDoesNotExistException userDoesNotExistException = mock(UserDoesNotExistException.class);
    when(userDoesNotExistException.getMessage()).thenReturn("User does not exist");
    var response = apiExceptionHandler.handleUserDoesNotExistException(userDoesNotExistException);
    assertThat(response.getBody()).isInstanceOf(ErrorResponse.class);
    ErrorResponse errorResponse = response.getBody();
    assertThat(errorResponse.getError()).isNotNull();
    assertThat(errorResponse.getError().getCodigo()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(errorResponse.getError().getDetail()).isEqualTo("User does not exist");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void handleInvalidTokenExist() {
    InvalidTokenException invalidTokenException = mock(InvalidTokenException.class);
    when(invalidTokenException.getMessage()).thenReturn("Invalid Token");
    var response = apiExceptionHandler.handleInvalidTokenException(invalidTokenException);
    assertThat(response.getBody()).isInstanceOf(ErrorResponse.class);
    ErrorResponse errorResponse = response.getBody();
    assertThat(errorResponse.getError()).isNotNull();
    assertThat(errorResponse.getError().getCodigo()).isEqualTo(HttpStatus.FORBIDDEN.value());
    assertThat(errorResponse.getError().getDetail()).isEqualTo("Invalid Token");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
  }

}
