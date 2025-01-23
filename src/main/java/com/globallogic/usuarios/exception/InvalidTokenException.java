package com.globallogic.usuarios.exception;

import lombok.Data;

@Data
public class InvalidTokenException extends Exception {
  private String message;

  public InvalidTokenException(String message) {
    super();
    this.message = message;
  }

}
