package com.banasthali.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.banasthali.backend.dto.AuthResponse;
import com.banasthali.backend.dto.driver.DriverLocationUpdateRequest;
import com.banasthali.backend.dto.driver.DriverLoginRequest;
import com.banasthali.backend.dto.driver.DriverRegisterRequest;
import com.banasthali.backend.dto.driver.DriverStatusUpdateRequest;
import com.banasthali.backend.dto.driver.DriverResponseDTO;
import com.banasthali.backend.model.Booking;
import com.banasthali.backend.service.DriverService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor

public class DriverController {

    private final DriverService driverService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(

            @RequestBody
            DriverRegisterRequest request) {

        return ResponseEntity.ok(

                driverService.registerDriver(
                        request
                )
        );
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(

            @RequestBody
            DriverLoginRequest request) {

        return ResponseEntity.ok(

                driverService.loginDriver(
                        request
                )
        );
    }


    @PatchMapping("/status")
    public ResponseEntity<DriverResponseDTO> updateStatus(

            Authentication auth,

            @RequestBody
            DriverStatusUpdateRequest request) {

        String email =
                auth.getName();

        return ResponseEntity.ok(

                driverService.updateOnlineStatus(

                        email,

                        request.getOnline()

                )
        );
    }


    @PutMapping("/location")
    public ResponseEntity<DriverResponseDTO> updateLocation(

            Authentication auth,

            @RequestBody
            DriverLocationUpdateRequest request) {

        String email =
                auth.getName();

        return ResponseEntity.ok(

                driverService.updateLocation(

                        email,

                        request

                )
        );
    }


    @GetMapping("/profile")
    public ResponseEntity<DriverResponseDTO> getProfile(

            Authentication auth) {

        String email =
                auth.getName();

        return ResponseEntity.ok(

                driverService.getDriverProfile(

                        email

                )
        );
    }


    @GetMapping("/rides")
    public ResponseEntity<List<Booking>> getRides(

            Authentication auth) {

        String email =
                auth.getName();

        return ResponseEntity.ok(

                driverService.getRideHistory(

                        email

                )
        );
    }

}