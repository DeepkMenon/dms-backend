package security;

import io.jsonwebtoken.*;
  import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

// Utility class to handle JWT creation, validation, and parsing
public class JwtTokenProvider {

  private final String jwtSecret = "HBXtaDT+6oVTryG1wu1NoJvR3lXr4M77tn5QHzXvalbiZ8hwxRntEWPode0ZiL7F1ndZi+9FeAA4xJO2KYU3lg=="; // use environment variables ideally
  private final long jwtExpirationMs = 3600000; // 1 hour

  public String getUsernameFromJWT(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(jwtSecret.getBytes())
      .build()
      .parseClaimsJws(token)
      .getBody()
      .getSubject();
  }

  public List<SimpleGrantedAuthority> getAuthorities(String token) {
    Claims claims = Jwts.parserBuilder()
      .setSigningKey(jwtSecret.getBytes())
      .build()
      .parseClaimsJws(token)
      .getBody();

    String roles = (String) claims.get("roles");

    return Arrays.stream(roles.split(","))
      .map(SimpleGrantedAuthority::new)
      .collect(Collectors.toList());
  }

  public boolean validateToken(String authToken) {
    try {
      Jwts.parserBuilder()
        .setSigningKey(jwtSecret.getBytes())
        .build()
        .parseClaimsJws(authToken);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      // log error if needed
    }
    return false;
  }
}
