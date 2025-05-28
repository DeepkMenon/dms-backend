package com.deepak.dms.user_service.controller;

import com.deepak.dms.user_service.dto.LoginRequest;
import com.deepak.dms.user_service.dto.RegisterRequest;
import com.deepak.dms.user_service.model.User;
import com.deepak.dms.user_service.service.UserService;
import com.deepak.dms.user_service.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final JwtTokenUtil jwtTokenUtil;

  @Autowired
  public AuthController(AuthenticationManager authenticationManager,
                        UserService userService,
                        JwtTokenUtil jwtTokenUtil) {
    this.authenticationManager = authenticationManager;
    this.userService = userService;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @PostMapping("/register")
  public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
    User user = userService.registerUser(request.getUsername(), request.getPassword(), request.getRole());
    return ResponseEntity.ok(user);
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
    );

    String token = jwtTokenUtil.generateToken((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal());

    return ResponseEntity.ok(Map.of("token", token));
  }

  @GetMapping("/users")
  public ResponseEntity<?> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }
}
