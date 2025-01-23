package com.globallogic.usuarios.service;

import com.globallogic.usuarios.model.UserRequest;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

  @Value("${app.security.jwtExpirationMs}")
  private int jwtExpirationMs;

  private final SecretKey secretKey;

  public String generateToken(String email) {

    return Jwts.builder()
        .subject(email)
        .issuedAt(new Date())
        .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(secretKey)
        .compact();
  }
}
