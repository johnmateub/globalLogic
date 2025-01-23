package com.globallogic.usuarios.controller;

import com.globallogic.usuarios.exception.InvalidTokenException;
import com.globallogic.usuarios.exception.UserDoesNotExistException;
import com.globallogic.usuarios.exception.UserExistsException;
import com.globallogic.usuarios.model.PhoneRequest;
import com.globallogic.usuarios.model.UserRequest;
import com.globallogic.usuarios.model.UserResponse;
import com.globallogic.usuarios.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserControllerTest {
  @InjectMocks
  UserController userController;

  @Mock
  UserService userService;

  @Test
  void signup_success() throws UserExistsException {
    List<PhoneRequest> phones = List.of(PhoneRequest.builder()
        .cityCode(1)
        .countryCode("+57")
        .number(12345678L).build());
    UserRequest userRequest = UserRequest.builder()
        .email("test@gmail.com")
        .name("John")
        .password("a3Fsdas8asdf")
        .phones(phones)
        .build();
    UserResponse userResponse = UserResponse.builder()
        .email("test@gmail.com")
        .name("John")
        .password("a3Fsdas8asdf")
        .phones(phones)
        .created(LocalDate.now())
        .lastLogin(LocalDateTime.now())
        .isActive(true)
        .build();
    when(userService.createUser(userRequest)).thenReturn(userResponse);
    ResponseEntity<UserResponse> response = userController.signup(userRequest);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isInstanceOf(UserResponse.class);
    assertThat(response.getBody()).isEqualTo(userResponse);
  }

  @Test
  void login() throws InvalidTokenException, UserDoesNotExistException {
    UUID id = UUID.randomUUID();
    List<PhoneRequest> phones = List.of(PhoneRequest.builder()
        .cityCode(1)
        .countryCode("+57")
        .number(12345678L).build());
    UserResponse userResponse = UserResponse.builder()
        .id(id)
        .token("token")
        .email("test@gmail.com")
        .name("John")
        .password("a3Fsdas8asdf")
        .phones(phones)
        .created(LocalDate.now())
        .lastLogin(LocalDateTime.now())
        .isActive(true)
        .build();

    when(userService.loginUser(id, "token")).thenReturn(userResponse);
    ResponseEntity<UserResponse> response = userController.login(id, "token");

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isInstanceOf(UserResponse.class);
    assertThat(response.getBody()).isEqualTo(userResponse);
  }
}
