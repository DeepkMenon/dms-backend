package com.deepak.dms.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  // Used by AuthController for login authentication
  @Bean
  public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return new ProviderManager(Collections.singletonList(provider));
  }

  // Required to encode passwords
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // Disables CSRF and configures public and secure endpoints
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable()) // ❗ Disable CSRF since we're stateless and using JWT
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/auth/**").permitAll() // 🔓 Allow public access to login/register
        .anyRequest().authenticated() // 🔐 Protect other endpoints
      );
    return http.build();
  }
}
