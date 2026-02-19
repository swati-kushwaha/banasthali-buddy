package com.banasthali.backend.dto.driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverLocationDTO {
    private Double latitude;
    private Double longitude;
}
