//package com.deepak.dms.filter;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.util.Date;
//import java.util.List;
//
//@Component
//public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
//
//  @Value("${jwt.secret}")
//  private String secretKey;
//
//  private static final List<String> openApiEndpoints = List.of(
//    "/api/auth"
//  );
//
//  private Key getSigningKey() {
//    byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
//    return Keys.hmacShaKeyFor(keyBytes);
//  }
//
//  private boolean isSecured(ServerHttpRequest request) {
//    String path = request.getURI().getPath();
//    return openApiEndpoints.stream()
//      .noneMatch(path::startsWith); // matches all /api/auth/**
//  }
//
//  private boolean isTokenExpired(Claims claims) {
//    return claims.getExpiration().before(new Date());
//  }
//
//  @Override
//  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//    ServerHttpRequest request = exchange.getRequest();
//
//    if (!isSecured(request)) {
//      return chain.filter(exchange);
//    }
//
//    if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//      return exchange.getResponse().setComplete();
//    }
//
//    String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//      return exchange.getResponse().setComplete();
//    }
//
//    String token = authHeader.substring(7);
//    try {
//      Claims claims = Jwts.parserBuilder()
//        .setSigningKey(getSigningKey())
//        .build()
//        .parseClaimsJws(token)
//        .getBody();
//
//      // Check if token is expired
//      if (isTokenExpired(claims)) {
//        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//        return exchange.getResponse().setComplete();
//      }
//
//      // Add user information to headers for downstream services
//      ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
//        .header("X-Auth-Username", claims.getSubject())
//        .header("X-Auth-Roles", claims.get("role", String.class))
//        .build();
//
//      return chain.filter(exchange.mutate().request(modifiedRequest).build());
//    } catch (Exception e) {
//      System.err.println("JWT Authentication failed: " + e.getMessage());
//      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//      return exchange.getResponse().setComplete();
//    }
//  }
//
//  @Override
//  public int getOrder() {
//    return -1; // Ensures this runs before other filters
//  }
//}
