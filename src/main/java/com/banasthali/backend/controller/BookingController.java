package com.banasthali.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banasthali.backend.dto.BookingRequest;
import com.banasthali.backend.dto.StatusUpdateRequest;
import com.banasthali.backend.model.Booking;
import com.banasthali.backend.model.User;
import com.banasthali.backend.repository.BookingRepository;
import com.banasthali.backend.service.BookingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Bookings", description = "E-rickshaw booking endpoints")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    @PostMapping("/request")
    @PreAuthorize("hasRole('STUDENT') or hasRole('PASSENGER')")
    @Operation(summary = "Request a booking from a pickup post")
    public ResponseEntity<Booking> requestBooking(@AuthenticationPrincipal User passenger,
                                                  @RequestBody BookingRequest request) {
        if (passenger == null) return ResponseEntity.status(401).build();
        Booking b = bookingService.requestBooking(passenger.getId(), request.getPickupPostId(), request.getDestinationPostId());
        return ResponseEntity.status(201).body(b);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Driver updates booking status")
    public ResponseEntity<Booking> updateStatus(@PathVariable String id,
                                                @AuthenticationPrincipal User driver,
                                                @RequestBody StatusUpdateRequest req) {
        if (driver == null) return ResponseEntity.status(401).build();
        Booking.BookingStatus status = Booking.BookingStatus.valueOf(req.getStatus());
        Booking b = bookingService.updateStatus(id, status, driver.getId());
        return ResponseEntity.ok(b);
    }

    @GetMapping("/me/passenger")
    @PreAuthorize("hasRole('STUDENT') or hasRole('PASSENGER')")
    public ResponseEntity<List<Booking>> myPassengerBookings(@AuthenticationPrincipal User passenger) {
        if (passenger == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(bookingRepository.findByPassengerId(passenger.getId()));
    }

    @GetMapping("/me/driver")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<List<Booking>> myDriverBookings(@AuthenticationPrincipal User driver) {
        if (driver == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(bookingRepository.findByDriverId(driver.getId()));
    }
}
