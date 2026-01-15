package com.banasthali.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.banasthali.backend.model.Post;

public interface PostRepository extends MongoRepository<Post, String> {

}
