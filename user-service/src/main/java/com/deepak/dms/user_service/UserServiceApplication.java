package com.deepak.dms.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
  "com.deepak.dms.user_service.controller",
  "com.deepak.dms.user_service.config",
  "com.deepak.dms.user_service.security",
  "com.deepak.dms.user_service.service",
  "com.deepak.dms.user_service.repository"
})
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
