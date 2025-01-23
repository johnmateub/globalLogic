package com.globallogic.usuarios.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "USER")
@ToString
@EqualsAndHashCode
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue
  private Integer id;

  private UUID idUser;

  private String name;

  private String email;

  private String password;

  private String phones;

  private LocalDate created;

  private LocalDateTime lastLogin;

  private String token;

  private Boolean isActive;

}
