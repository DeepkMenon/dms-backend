package com.deepak.dms.user_service.repository;

import com.deepak.dms.user_service.model.User;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
  Optional<User> findByUsername(String username);
  boolean existsByUsername(String username);
}
