package com.banasthali.backend.repository;

import com.banasthali.backend.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByPassengerId(String passengerId);
    List<Booking> findByDriverId(String driverId);
}
