package com.globallogic.usuarios.service;

import com.globallogic.usuarios.entity.User;
import com.globallogic.usuarios.exception.InvalidTokenException;
import com.globallogic.usuarios.exception.UserDoesNotExistException;
import com.globallogic.usuarios.exception.UserExistsException;
import com.globallogic.usuarios.model.UserRequest;
import com.globallogic.usuarios.model.UserResponse;
import com.globallogic.usuarios.model.mapper.UserMapper;
import com.globallogic.usuarios.repository.UserRepository;
import com.globallogic.usuarios.util.AESUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.spec.IvParameterSpec;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final JwtTokenService jwtTokenService;
  private final UserMapper userMapper;
  private final AESUtil aesUtil;
  private final IvParameterSpec ivParameterSpec;

  public UserResponse createUser(UserRequest userRequest) throws UserExistsException {
    Optional<User> existingUser = userRepository.findByEmail(userRequest.getEmail());
    if (existingUser.isPresent()) {
      throw new UserExistsException("Usuario ya existe.");
    }
    User user = userMapper.toUser(userRequest);
    user.setIdUser(UUID.randomUUID());
    user.setToken(jwtTokenService.generateToken(userRequest.getEmail()));
    user.setPassword(aesUtil.encryptPasswordBased(user.getPassword(), ivParameterSpec));
    User savedUser = userRepository.save(user);
    savedUser.setPassword(aesUtil.decryptPasswordBased(user.getPassword(), ivParameterSpec));
    return userMapper.toUserResponse(savedUser);
  }

  public UserResponse loginUser(UUID id, String token) throws UserDoesNotExistException, InvalidTokenException {
    Optional<User> user = userRepository.findByIdUser(id);
    if (user.isEmpty()) {
      throw new UserDoesNotExistException("User does not exist.");
    }
    User userDb = user.get();
    if (!userDb.getToken().equals(token)) {
      throw new InvalidTokenException("Invalid Token");
    }
    userDb.setToken(jwtTokenService.generateToken(userDb.getEmail()));
    userDb.setPassword(aesUtil.decryptPasswordBased(userDb.getPassword(), ivParameterSpec));
    UserResponse response = userMapper.toUserResponse(userDb);
    userDb.setLastLogin(LocalDateTime.now());
    userRepository.save(userDb);
    return response;
  }
}
