package com.banasthali.backend.service.impl;

import java.util.List;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.banasthali.backend.dto.AuthResponse;
import com.banasthali.backend.dto.driver.DriverLocationUpdateRequest;
import com.banasthali.backend.dto.driver.DriverLoginRequest;
import com.banasthali.backend.dto.driver.DriverRegisterRequest;
import com.banasthali.backend.model.Booking;
import com.banasthali.backend.model.Driver;
import com.banasthali.backend.repository.BookingRepository;
import com.banasthali.backend.repository.DriverRepository;
import com.banasthali.backend.security.JwtTokenProvider;
import com.banasthali.backend.service.DriverService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final BookingRepository bookingRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public AuthResponse registerDriver(DriverRegisterRequest request) {
        if (driverRepository.findByEmail(request.getEmail()).isPresent()) {
            return AuthResponse.message("Email already registered");
        }

        Driver driver = Driver.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .vehicleNumber(request.getVehicleNumber())
                .licenseNumber(request.getLicenseNumber())
                .isOnline(false)
                .build();

        Driver saved = driverRepository.save(driver);

        String token = jwtTokenProvider.generateToken(saved.getEmail(), saved.getId(), "DRIVER");
        return AuthResponse.success(token, saved.getId(), saved.getName(), saved.getEmail());
    }

    @Override
    public AuthResponse loginDriver(DriverLoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            return AuthResponse.message("Invalid email or password");
        }

        Driver driver = driverRepository.findByEmail(request.getEmail()).orElse(null);
        if (driver == null) return AuthResponse.message("Driver not found");

        String token = jwtTokenProvider.generateToken(driver.getEmail(), driver.getId(), "DRIVER");
        return AuthResponse.success(token, driver.getId(), driver.getName(), driver.getEmail());
    }

    @Override
    public com.banasthali.backend.dto.driver.DriverResponseDTO updateOnlineStatus(String driverId, boolean online) {
        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        driver.setIsOnline(online);
        Driver saved = driverRepository.save(driver);
        // notify subscribers about status change (broadcast DTO)
        com.banasthali.backend.dto.driver.DriverResponseDTO dto = com.banasthali.backend.mapper.DriverMapper.toDTO(saved);
        messagingTemplate.convertAndSend("/topic/driver-location/" + driverId, dto);
        return dto;
    }

    @Override
    public com.banasthali.backend.dto.driver.DriverResponseDTO updateLocation(String driverId, DriverLocationUpdateRequest request) {
        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        GeoJsonPoint point = new GeoJsonPoint(request.getLongitude(), request.getLatitude());
        driver.setLocation(point);
        Driver saved = driverRepository.save(driver);

        com.banasthali.backend.dto.driver.DriverResponseDTO dto = com.banasthali.backend.mapper.DriverMapper.toDTO(saved);
        messagingTemplate.convertAndSend("/topic/driver-location/" + driverId, dto);
        return dto;
    }

    @Override
    public com.banasthali.backend.dto.driver.DriverResponseDTO getDriverProfile(String driverId) {
        Driver d = driverRepository.findById(driverId).orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        return com.banasthali.backend.mapper.DriverMapper.toDTO(d);
    }

    @Override
    public List<Booking> getRideHistory(String driverId) {
        return bookingRepository.findByDriverId(driverId);
    }
}
