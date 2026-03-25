package com.banasthali.backend.service;

import java.util.List;

import com.banasthali.backend.dto.AuthResponse;
import com.banasthali.backend.dto.driver.DriverLocationUpdateRequest;
import com.banasthali.backend.dto.driver.DriverLoginRequest;
import com.banasthali.backend.dto.driver.DriverRegisterRequest;
import com.banasthali.backend.dto.driver.DriverResponseDTO;
import com.banasthali.backend.model.Booking;

public interface DriverService {

    AuthResponse registerDriver(
            DriverRegisterRequest request
    );

    AuthResponse loginDriver(
            DriverLoginRequest request
    );

    DriverResponseDTO updateOnlineStatus(
            String email,
            boolean online
    );

    DriverResponseDTO updateLocation(
            String email,
            DriverLocationUpdateRequest request
    );

    DriverResponseDTO getDriverProfile(
            String email
    );

    List<Booking> getRideHistory(
            String email
    );

}