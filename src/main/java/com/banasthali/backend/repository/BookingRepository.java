package com.banasthali.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.banasthali.backend.model.Booking;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByPassengerId(String passengerId);
    List<Booking> findByDriverId(String driverId);
}
