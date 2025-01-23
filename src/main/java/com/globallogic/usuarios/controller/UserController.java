package com.globallogic.usuarios.controller;

import com.globallogic.usuarios.exception.InvalidTokenException;
import com.globallogic.usuarios.exception.UserDoesNotExistException;
import com.globallogic.usuarios.exception.UserExistsException;
import com.globallogic.usuarios.model.UserRequest;
import com.globallogic.usuarios.model.UserResponse;
import com.globallogic.usuarios.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping(value = "/sign-up", produces = {"application/json"},
      consumes = {"application/json"})
  public ResponseEntity<UserResponse> signup(@Valid @RequestBody UserRequest userRequest) throws UserExistsException {
    UserResponse userResponse = userService.createUser(userRequest);
    return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
  }

  @GetMapping (value = "/login/{id}", produces = {"application/json"})
  public ResponseEntity<UserResponse> login(@PathVariable UUID id, @RequestHeader("token") String token)
      throws InvalidTokenException, UserDoesNotExistException {
    UserResponse userResponse = userService.loginUser(id, token);
    return new ResponseEntity<>(userResponse, HttpStatus.OK);
  }

}
