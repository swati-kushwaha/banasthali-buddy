package com.banasthali.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.banasthali.backend.model.ERickshawBooking;

public interface ERickshawBookingRepository extends MongoRepository<ERickshawBooking, String> {

}
