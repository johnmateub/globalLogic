package com.globallogic.usuarios.model.mapper;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.globallogic.usuarios.entity.User;
import com.globallogic.usuarios.model.PhoneRequest;
import com.globallogic.usuarios.model.UserRequest;
import com.globallogic.usuarios.model.UserResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMapperTest {

  private final ObjectMapper objectMapper = new ObjectMapper().registerModules(new JavaTimeModule())
      .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

  UserMapper mapper = new UserMapperImpl();

  @Test
  void toUser() throws JsonProcessingException {
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

    User expected = User.builder()
        .email("test@gmail.com")
        .name("John")
        .password("a3Fsdas8asdf")
        .isActive(true)
        .phones(objectMapper.writeValueAsString(phones))
        .build();

    User user = mapper.toUser(userRequest);

    assertThat(user).usingRecursiveComparison()
        .ignoringFields("id", "created", "lastLogin")
        .isEqualTo(expected);

  }

  @Test
  void toUser_whenUserRequestIsNull() {
    User user = mapper.toUser(null);
    assertThat(user).isNull();
  }

  @Test
  void toUserResponse() throws JsonProcessingException {
    LocalDate created = LocalDate.now();
    LocalDateTime lastLogin = LocalDateTime.now();
    List<PhoneRequest> phonesRequest = List.of(PhoneRequest.builder()
        .cityCode(1)
        .countryCode("+57")
        .number(12345678L).build());

    String phones = objectMapper.writeValueAsString(phonesRequest);

    User user = User.builder()
        .email("test@gmail.com")
        .name("John")
        .password("a3Fsdas8asdf")
        .isActive(true)
        .phones(phones)
        .created(created)
        .lastLogin(lastLogin)
        .build();

    UserResponse expectedResponse = UserResponse.builder()
        .email("test@gmail.com")
        .name("John")
        .password("a3Fsdas8asdf")
        .phones(phonesRequest)
        .isActive(true)
        .created(created)
        .lastLogin(lastLogin)
        .build();

    UserResponse userResponse = mapper.toUserResponse(user);

    assertThat(userResponse).isEqualTo(expectedResponse);
  }

  @Test
  void toUserResponse_whenUserIsNull() {
    UserResponse userResponse = mapper.toUserResponse(null);
    assertThat(userResponse).isNull();
  }

}
