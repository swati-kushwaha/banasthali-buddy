package com.banasthali.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.banasthali.backend.model.Driver;

@Repository
public interface DriverRepository extends MongoRepository<Driver, String> {
    Optional<Driver> findByEmail(String email);

    // Find online drivers near a point within distance
    List<Driver> findByIsOnlineTrueAndLocationNear(Point location, Distance distance);
}
