package com.banasthali.backend.repository;

import com.banasthali.backend.model.Message;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository
        extends MongoRepository<Message,String> {

    List<Message> findByBookingIdOrderByTimestampAsc(
            String bookingId
    );
}