package com.deepak.dms.document_service.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Base64;

@Component
public class JwtTokenProvider {

  @Value("${jwt.secret}")
  private String jwtSecret;

  private final long jwtExpirationMs = 3600000; // 1 hour

  private SecretKey getSigningKey() {
    byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String getUsernameFromJWT(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(getSigningKey())
      .build()
      .parseClaimsJws(token)
      .getBody()
      .getSubject();
  }

  public List<SimpleGrantedAuthority> getAuthorities(String token) {
    Claims claims = Jwts.parserBuilder()
      .setSigningKey(getSigningKey())
      .build()
      .parseClaimsJws(token)
      .getBody();

    String roles = claims.get("roles", String.class);
    if (roles == null || roles.isEmpty()) {
      return Collections.emptyList();
    }

    return Arrays.stream(roles.split(","))
      .map(SimpleGrantedAuthority::new)
      .collect(Collectors.toList());
  }

  public boolean validateToken(String authToken) {
    try {
      Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(authToken);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      System.out.println("Invalid JWT: " + e.getMessage());
    }
    return false;
  }
}
