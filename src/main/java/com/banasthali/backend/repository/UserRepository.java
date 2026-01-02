package com.banasthali.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.banasthali.backend.model.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
