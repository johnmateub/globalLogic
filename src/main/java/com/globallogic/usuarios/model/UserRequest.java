package com.globallogic.usuarios.model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
  private String name;

  @Pattern(regexp = "^(.+)@(\\S+)$",
      message = "must be a valid email")
  private String email;

  @Pattern(regexp = "^(?=.*[a-z])((?=.*[A-Z]){1})((?=.*\\d){2})[a-zA-Z\\d]{8,12}$", message = "must be alphabetic and upper case")
  @Size(max = 12, min = 8)
  private String password;

  private List<PhoneRequest> phones;

}
