package com.deepak.dms.user_service.controller;


import com.deepak.dms.user_service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.deepak.dms.user_service.security.JwtTokenUtil;
import com.deepak.dms.user_service.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserService userService;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestParam String username,
                                    @RequestParam String password,
                                    @RequestParam(defaultValue = "USER") String role) {
    User user = userService.registerUser(username, password, role);
    return ResponseEntity.ok(user);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(username, password)
    );

    final UserDetails userDetails = userService.loadUserByUsername(username);
    final String token = jwtTokenUtil.generateToken(userDetails);

    Map<String, String> response = new HashMap<>();
    response.put("token", token);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/users")
  public ResponseEntity<?> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }


}
