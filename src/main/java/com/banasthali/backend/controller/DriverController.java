package com.banasthali.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banasthali.backend.dto.AuthResponse;
import com.banasthali.backend.dto.driver.DriverLocationUpdateRequest;
import com.banasthali.backend.dto.driver.DriverLoginRequest;
import com.banasthali.backend.dto.driver.DriverRegisterRequest;
import com.banasthali.backend.dto.driver.DriverStatusUpdateRequest;
import com.banasthali.backend.model.Booking;
import com.banasthali.backend.model.Driver;
import com.banasthali.backend.repository.DriverRepository;
import com.banasthali.backend.service.DriverService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private final DriverRepository driverRepository;

    @PostMapping("/register")
    @Operation(summary = "Register driver", description = "Register a new driver. Returns JWT token on success.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Driver registered and token returned", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
        @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody DriverRegisterRequest request) {
        AuthResponse response = driverService.registerDriver(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Driver login", description = "Authenticate driver and return JWT token.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authenticated", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
        @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
        @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody DriverLoginRequest request) {
        AuthResponse response = driverService.loginDriver(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/status")
    @Operation(summary = "Update online status", description = "Update driver's online/offline status. Requires driver authentication.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status updated", content = @Content(schema = @Schema(implementation = DriverResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
        @ApiResponse(responseCode = "404", description = "Driver not found", content = @Content)
    })
    public ResponseEntity<com.banasthali.backend.dto.driver.DriverResponseDTO> updateStatus(@Valid @RequestBody DriverStatusUpdateRequest request) {
        String email = currentUserEmail();
        com.banasthali.backend.model.Driver driver = driverRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        com.banasthali.backend.dto.driver.DriverResponseDTO updated = driverService.updateOnlineStatus(driver.getId(), request.getOnline());
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/location")
    @Operation(summary = "Update location", description = "Update driver's geo location. Broadcasts location DTO to subscribers on success.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Location updated", content = @Content(schema = @Schema(implementation = DriverResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
        @ApiResponse(responseCode = "404", description = "Driver not found", content = @Content)
    })
    public ResponseEntity<com.banasthali.backend.dto.driver.DriverResponseDTO> updateLocation(@Valid @RequestBody DriverLocationUpdateRequest request) {
        String email = currentUserEmail();
        com.banasthali.backend.model.Driver driver = driverRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        com.banasthali.backend.dto.driver.DriverResponseDTO updated = driverService.updateLocation(driver.getId(), request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/profile")
    @Operation(summary = "Get profile", description = "Retrieve authenticated driver's profile information.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Driver profile", content = @Content(schema = @Schema(implementation = DriverResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
        @ApiResponse(responseCode = "404", description = "Driver not found", content = @Content)
    })
    public ResponseEntity<com.banasthali.backend.dto.driver.DriverResponseDTO> getProfile() {
        String email = currentUserEmail();
        com.banasthali.backend.model.Driver driver = driverRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        com.banasthali.backend.dto.driver.DriverResponseDTO profile = driverService.getDriverProfile(driver.getId());
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/rides")
    @Operation(summary = "Get ride history", description = "Retrieve list of bookings for the authenticated driver.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of bookings", content = @Content(schema = @Schema(implementation = Booking.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
        @ApiResponse(responseCode = "404", description = "Driver not found", content = @Content)
    })
    public ResponseEntity<List<Booking>> getRides() {
        String email = currentUserEmail();
        Driver driver = driverRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        List<Booking> rides = driverService.getRideHistory(driver.getId());
        return ResponseEntity.ok(rides);
    }

    private String currentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new IllegalArgumentException("Unauthenticated");
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return String.valueOf(principal);
    }
}
