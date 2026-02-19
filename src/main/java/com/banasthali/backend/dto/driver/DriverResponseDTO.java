package com.banasthali.backend.dto.driver;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverResponseDTO {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String vehicleNumber;
    private DriverLocationDTO location;
    private Boolean isOnline;
    private Double rating;
    private LocalDateTime createdAt;
}
