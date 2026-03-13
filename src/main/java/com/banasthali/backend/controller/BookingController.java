package com.banasthali.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.banasthali.backend.dto.BookingRequest;
import com.banasthali.backend.dto.StatusUpdateRequest;
import com.banasthali.backend.model.Booking;
import com.banasthali.backend.model.User;
import com.banasthali.backend.repository.BookingRepository;
import com.banasthali.backend.repository.UserRepository;
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
    private final UserRepository userRepository;

    // STUDENT requests a ride
    @PostMapping("/request")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Student requests a booking from a pickup post")
    public ResponseEntity<Booking> requestBooking(@RequestBody BookingRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User student = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Booking booking = bookingService.requestBooking(
                student.getId(),
                request.getPickupPostId(),
                request.getDestinationPostId()
        );

        return ResponseEntity.status(201).body(booking);
    }

    // DRIVER updates booking status
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Driver updates booking status")
    public ResponseEntity<Booking> updateStatus(
            @PathVariable String id,
            @RequestBody StatusUpdateRequest req) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User driver = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        Booking.BookingStatus status = Booking.BookingStatus.valueOf(req.getStatus());

        Booking booking = bookingService.updateStatus(
                id,
                status,
                driver.getId()
        );

        return ResponseEntity.ok(booking);
    }

    // STUDENT booking history
    @GetMapping("/me/student")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Get bookings for authenticated student")
    public ResponseEntity<List<Booking>> myStudentBookings() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User student = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Booking> bookings = bookingRepository.findByPassengerId(student.getId());

        return ResponseEntity.ok(bookings);
    }

    // DRIVER booking history
    @GetMapping("/me/driver")
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Get bookings assigned to authenticated driver")
    public ResponseEntity<List<Booking>> myDriverBookings() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User driver = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        List<Booking> bookings = bookingRepository.findByDriverId(driver.getId());

        return ResponseEntity.ok(bookings);
    }
}