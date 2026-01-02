package com.banasthali.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.banasthali.backend.model.User;

public interface UserRepository extends MongoRepository<User, String> {
}
