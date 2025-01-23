package com.globallogic.usuarios.model;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

  Error error;

  @Builder
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @EqualsAndHashCode
  public static class Error {
    private Timestamp timestamp;
    private Integer codigo;
    private String detail;

  }
}
