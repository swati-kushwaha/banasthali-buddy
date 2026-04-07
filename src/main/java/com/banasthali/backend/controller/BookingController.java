package com.banasthali.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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


    // STUDENT REQUEST BOOKING
    @PostMapping("/request")
    @PreAuthorize("hasRole('STUDENT') or hasRole('PASSENGER')")
    @Operation(summary = "Request a booking from a pickup post")
    public ResponseEntity<Booking> requestBooking(

            @AuthenticationPrincipal User passenger,

            @RequestBody BookingRequest request
    ){

        if (passenger == null)

            return ResponseEntity.status(401).build();

        Booking booking = bookingService.requestBooking(

                passenger.getId(),

                request.getPickupPostId(),

                request.getDestinationPostId()

        );

        return ResponseEntity.status(201).body(booking);

    }


    // DRIVER UPDATE STATUS
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Driver updates booking status")
    public ResponseEntity<Booking> updateStatus(

            @PathVariable String id,

            @AuthenticationPrincipal User driver,

            @RequestBody StatusUpdateRequest request
    ){

        if (driver == null)

            return ResponseEntity.status(401).build();

        Booking.BookingStatus status =

                Booking.BookingStatus.valueOf(request.getStatus());

        Booking booking = bookingService.updateStatus(

                id,

                status,

                driver.getId()

        );

        return ResponseEntity.ok(booking);

    }


    // STUDENT BOOKINGS (NEW API)
    @GetMapping("/me/student")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Booking>> myStudentBookings(

            @AuthenticationPrincipal User student
    ){

        if (student == null)

            return ResponseEntity.status(401).build();

        return ResponseEntity.ok(

                bookingRepository.findByPassengerId(student.getId())

        );

    }


    // PASSENGER BOOKINGS (old)
    @GetMapping("/me/passenger")
    @PreAuthorize("hasRole('STUDENT') or hasRole('PASSENGER')")
    public ResponseEntity<List<Booking>> myPassengerBookings(

            @AuthenticationPrincipal User passenger
    ){

        if (passenger == null)

            return ResponseEntity.status(401).build();

        return ResponseEntity.ok(

                bookingRepository.findByPassengerId(passenger.getId())

        );

    }


    // DRIVER BOOKINGS
    @GetMapping("/me/driver")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<List<Booking>> myDriverBookings(

            @AuthenticationPrincipal User driver
    ){

        if (driver == null)

            return ResponseEntity.status(401).build();

        return ResponseEntity.ok(

                bookingRepository.findByDriverId(driver.getId())

        );

    }

    // GET ALL PENDING BOOKINGS FOR DRIVER
    @GetMapping
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Get all pending ride requests for driver")
    public ResponseEntity<List<Booking>> getPendingBookings() {

        return ResponseEntity.ok(

                bookingRepository.findByStatus(Booking.BookingStatus.PENDING)

        );

    }

}