package com.globallogic.usuarios.exception;

import lombok.Data;

@Data
public class UserExistsException extends Exception {
  private String message;

  public UserExistsException(String message) {
    super();
    this.message = message;
  }

}
