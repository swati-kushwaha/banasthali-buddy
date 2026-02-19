package com.banasthali.backend.service;

import java.util.List;

import com.banasthali.backend.dto.AuthResponse;
import com.banasthali.backend.dto.driver.DriverLocationUpdateRequest;
import com.banasthali.backend.dto.driver.DriverLoginRequest;
import com.banasthali.backend.dto.driver.DriverRegisterRequest;
import com.banasthali.backend.dto.driver.DriverResponseDTO;
import com.banasthali.backend.model.Booking;

public interface DriverService {
    AuthResponse registerDriver(DriverRegisterRequest request);
    AuthResponse loginDriver(DriverLoginRequest request);
    DriverResponseDTO updateOnlineStatus(String driverId, boolean online);
    DriverResponseDTO updateLocation(String driverId, DriverLocationUpdateRequest request);
    DriverResponseDTO getDriverProfile(String driverId);
    List<Booking> getRideHistory(String driverId);
}
