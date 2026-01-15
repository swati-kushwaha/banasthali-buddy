package com.banasthali.backend.dto;

import lombok.Data;

@Data
public class BookingRequest {
    private String pickupPostId;
    private String destinationPostId;
}
