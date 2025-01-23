package com.globallogic.usuarios.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
  private UUID id;
  private LocalDate created;
  private LocalDateTime lastLogin;
  private String token;
  private Boolean isActive;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private String name;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private String email;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private String password;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<PhoneRequest> phones;
}
