package com.banasthali.backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    private String id;

    private String passengerId;
    private String driverId;
    private String pickupPostId;
    private String destinationPostId;

    private BookingStatus status;

    public enum BookingStatus {
        PENDING, ACCEPTED, STARTED, ARRIVED, COMPLETED, CANCELLED
    }
}
