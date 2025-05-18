package com.deepak.dms.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;


@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String SECRET_KEY;

  public void validateToken(final String token) {
    Jwts.parser()
      .setSigningKey(SECRET_KEY)
      .parseClaimsJws(token);
  }

  public Claims getClaims(final String token) {
    return Jwts.parser()
      .setSigningKey(SECRET_KEY)
      .parseClaimsJws(token)
      .getBody();
  }
}
