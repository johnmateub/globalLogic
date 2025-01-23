package com.globallogic.usuarios.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.globallogic.usuarios.entity.User;
import com.globallogic.usuarios.exception.InvalidTokenException;
import com.globallogic.usuarios.exception.UserDoesNotExistException;
import com.globallogic.usuarios.exception.UserExistsException;
import com.globallogic.usuarios.model.PhoneRequest;
import com.globallogic.usuarios.model.UserRequest;
import com.globallogic.usuarios.model.UserResponse;
import com.globallogic.usuarios.model.mapper.UserMapper;
import com.globallogic.usuarios.repository.UserRepository;
import com.globallogic.usuarios.util.AESUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.crypto.spec.IvParameterSpec;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

  @InjectMocks
  private UserService userService;
  @Mock
  private UserRepository userRepository;
  @Mock
  private JwtTokenService jwtTokenService;
  @Mock
  private AESUtil aesUtil;
  @Spy
  private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
  @Mock
  private IvParameterSpec ivParameterSpec;

  private final ObjectMapper objectMapper = new ObjectMapper().registerModules(new JavaTimeModule())
      .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

  @Test
  void createUser_success() throws UserExistsException, JsonProcessingException {
    List<PhoneRequest> phonesRequest = List.of(PhoneRequest.builder()
        .cityCode(1)
        .countryCode("+57")
        .number(12345678L).build());

    UserRequest userRequest = UserRequest.builder()
        .email("test@gmail.com")
        .name("John")
        .password("a3Fsdas8asdf")
        .phones(phonesRequest)
        .build();
    UserResponse expectedResponse = UserResponse.builder()
        .email("test@gmail.com")
        .name("John")
        .password("a3Fsdas8asdf")
        .phones(phonesRequest)
        .isActive(true)
        .build();

    String phones = objectMapper.writeValueAsString(phonesRequest);
    User user = User.builder()
        .email("test@gmail.com")
        .name("John")
        .password("a3Fsdas8asdf")
        .isActive(true)
        .phones(phones)
        .build();

    when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
    when(jwtTokenService.generateToken(userRequest.getEmail())).thenReturn("token");
    when(userRepository.save(any())).thenReturn(user);
    when(aesUtil.encryptPasswordBased(userRequest.getPassword(), ivParameterSpec)).thenReturn("ASDX123456");
    when(aesUtil.decryptPasswordBased("ASDX123456", ivParameterSpec)).thenReturn(userRequest.getPassword());
    UserResponse userResponse = userService.createUser(userRequest);

    assertThat(userResponse).usingRecursiveComparison()
        .ignoringFields("id", "created", "lastLogin")
        .isEqualTo(expectedResponse);
  }

  @Test
  void createUser_throwException_whenExistingUser() {
    UserRequest userRequest = UserRequest.builder()
        .email("test@gmail.com")
        .build();
    User user = User.builder()
        .idUser(UUID.randomUUID())
        .email("test@gmail.com")
        .build();

    when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(user));

    assertThatThrownBy(() -> userService.createUser(userRequest)).isInstanceOf(UserExistsException.class);
  }

  @Test
  void loginUser_success() throws JsonProcessingException, InvalidTokenException, UserDoesNotExistException {
    UUID id = UUID.randomUUID();
    List<PhoneRequest> phonesRequest = List.of(PhoneRequest.builder()
        .cityCode(1)
        .countryCode("+57")
        .number(12345678L).build());

    UserRequest userRequest = UserRequest.builder()
        .email("test@gmail.com")
        .name("John")
        .password("a3Fsdas8asdf")
        .phones(phonesRequest)
        .build();
    UserResponse expectedResponse = UserResponse.builder()
        .id(id)
        .email("test@gmail.com")
        .name("John")
        .password("a3Fsdas8asdf")
        .phones(phonesRequest)
        .isActive(true)
        .build();

    String phones = objectMapper.writeValueAsString(phonesRequest);

    User user = User.builder()
        .idUser(id)
        .email("test@gmail.com")
        .name("John")
        .password("a3Fsdas8asdf")
        .isActive(true)
        .token("token")
        .phones(phones)
        .build();

    when(userRepository.findByIdUser(id)).thenReturn(Optional.of(user));
    when(jwtTokenService.generateToken(userRequest.getEmail())).thenReturn("tokenUpdated");
    when(aesUtil.decryptPasswordBased(any(), any())).thenReturn(userRequest.getPassword());
    UserResponse userResponse = userService.loginUser(id, "token");

    assertThat(userResponse).usingRecursiveComparison()
        .ignoringFields("created", "lastLogin", "token")
        .isEqualTo(expectedResponse);
  }

  @Test
  void loginUser_throwException_whenUserDoesNotExist() {
    UUID id = UUID.randomUUID();

    when(userRepository.findByIdUser(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.loginUser(id, "token")).isInstanceOf(UserDoesNotExistException.class);
  }

  @Test
  void loginUser_throwException_whenInvalidToken() {
    UUID id = UUID.randomUUID();
    User user = User.builder()
        .idUser(id)
        .email("test@gmail.com")
        .name("John")
        .password("a3Fsdas8asdf")
        .isActive(true)
        .token("token")
        .build();


    when(userRepository.findByIdUser(id)).thenReturn(Optional.of(user));

    assertThatThrownBy(() -> userService.loginUser(id, "newToken")).isInstanceOf(InvalidTokenException.class);
  }

}
