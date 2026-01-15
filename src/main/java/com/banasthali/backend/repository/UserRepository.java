package com.banasthali.backend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.banasthali.backend.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    java.util.List<User> findByRoleAndDriverAvailableTrue(String role);
}
