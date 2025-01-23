package com.globallogic.usuarios.exception;

import lombok.Data;

@Data
public class UserDoesNotExistException extends Exception {
  private String message;

  public UserDoesNotExistException(String message) {
    super();
    this.message = message;
  }

}
