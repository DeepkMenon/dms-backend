package com.deepak.dms.user_service.service;

import com.deepak.dms.user_service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.deepak.dms.user_service.repository.UserRepository;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  // Used by Spring Security to authenticate
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
      .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

    return new org.springframework.security.core.userdetails.User(
      user.getUsername(),
      user.getPassword(),
      Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
    );
  }

  // Used to register new users
  public User registerUser(String username, String password, String role) {
    if (userRepository.findByUsername(username).isPresent()) {
      throw new RuntimeException("Username already taken");
    }

    User newUser = new User();
    newUser.setUsername(username);
    newUser.setPassword(passwordEncoder.encode(password));
    newUser.setRole(role.toUpperCase());

    return userRepository.save(newUser);
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }
}
