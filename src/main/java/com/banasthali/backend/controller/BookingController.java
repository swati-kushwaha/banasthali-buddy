package com.banasthali.backend.controller;

import com.banasthali.backend.dto.BookingRequest;
import com.banasthali.backend.dto.StatusUpdateRequest;
import com.banasthali.backend.model.Booking;
import com.banasthali.backend.model.User;
import com.banasthali.backend.repository.BookingRepository;
import com.banasthali.backend.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Bookings", description = "E-rickshaw booking endpoints")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    @PostMapping("/request")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Request a booking from a pickup post")
    public ResponseEntity<Booking> requestBooking(@AuthenticationPrincipal User passenger,
                                                  @RequestBody BookingRequest request) {
        Booking b = bookingService.requestBooking(passenger.getId(), request.getPickupPostId(), request.getDestinationPostId());
        return ResponseEntity.status(201).body(b);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Driver updates booking status")
    public ResponseEntity<Booking> updateStatus(@PathVariable String id,
                                                @AuthenticationPrincipal User driver,
                                                @RequestBody StatusUpdateRequest req) {
        Booking.BookingStatus status = Booking.BookingStatus.valueOf(req.getStatus());
        Booking b = bookingService.updateStatus(id, status, driver.getId());
        return ResponseEntity.ok(b);
    }

    @GetMapping("/me/passenger")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Booking>> myPassengerBookings(@AuthenticationPrincipal User passenger) {
        return ResponseEntity.ok(bookingRepository.findByPassengerId(passenger.getId()));
    }

    @GetMapping("/me/driver")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<List<Booking>> myDriverBookings(@AuthenticationPrincipal User driver) {
        return ResponseEntity.ok(bookingRepository.findByDriverId(driver.getId()));
    }
}
package com.banasthali.backend.controller;

import com.banasthali.backend.model.Booking;
import com.banasthali.backend.model.User;
import com.banasthali.backend.repository.BookingRepository;
import com.banasthali.backend.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Bookings", description = "Booking workflow endpoints")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    @PostMapping("/request")
    @PreAuthorize("hasRole('PASSENGER') or hasRole('STUDENT')")
    @Operation(summary = "Passenger initiates a booking from a post")
    public ResponseEntity<Booking> request(@RequestBody BookingRequest body) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String passengerId = principal instanceof User ? ((User) principal).getId() : null;
        if (passengerId == null) return ResponseEntity.status(401).build();

        Booking b = bookingService.requestBooking(passengerId, body.getPickupPostId(), body.getDestinationPostId());
        return ResponseEntity.ok(b);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Driver updates ride status")
    public ResponseEntity<Booking> updateStatus(@PathVariable String id, @RequestBody StatusUpdate body) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String driverId = principal instanceof User ? ((User) principal).getId() : null;
        if (driverId == null) return ResponseEntity.status(401).build();

        Booking.BookingStatus status = Booking.BookingStatus.valueOf(body.getStatus());
        Booking updated = bookingService.updateStatus(id, status, driverId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/me/passenger")
    @PreAuthorize("hasRole('PASSENGER') or hasRole('STUDENT')")
    public List<Booking> myBookingsAsPassenger() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String passengerId = principal instanceof User ? ((User) principal).getId() : null;
        return bookingRepository.findByPassengerId(passengerId);
    }

    @GetMapping("/me/driver")
    @PreAuthorize("hasRole('DRIVER')")
    public List<Booking> myBookingsAsDriver() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String driverId = principal instanceof User ? ((User) principal).getId() : null;
        return bookingRepository.findByDriverId(driverId);
    }

    // DTOs
    public static class BookingRequest {
        private String pickupPostId;
        private String destinationPostId;

        public String getPickupPostId() { return pickupPostId; }
        public void setPickupPostId(String p) { this.pickupPostId = p; }
        public String getDestinationPostId() { return destinationPostId; }
        public void setDestinationPostId(String d) { this.destinationPostId = d; }
    }

    public static class StatusUpdate {
        private String status;
        public String getStatus() { return status; }
        public void setStatus(String s) { this.status = s; }
    }
}
