package com.deepak.dms.user_service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

  @Value("${jwt.secret}")
  private String SECRET_KEY;

//  private final String SECRET_KEY = "yourSecretKeyHere"; // Replace with a strong secret and store securely
  private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

  public String generateToken(UserDetails userDetails) {
    return Jwts.builder()
      .setSubject(userDetails.getUsername())
      .claim("role", userDetails.getAuthorities().stream()
        .findFirst().orElseThrow().getAuthority())
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
      .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
      .compact();
  }
}
