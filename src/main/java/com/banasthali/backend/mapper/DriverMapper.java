package com.banasthali.backend.mapper;

import com.banasthali.backend.dto.driver.DriverLocationDTO;
import com.banasthali.backend.dto.driver.DriverResponseDTO;
import com.banasthali.backend.model.Driver;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

public final class DriverMapper {

    private DriverMapper() {}

    public static DriverResponseDTO toDTO(Driver d) {
        if (d == null) return null;

        DriverLocationDTO loc = null;
        GeoJsonPoint p = d.getLocation();
        if (p != null) {
            loc = new DriverLocationDTO(p.getY(), p.getX());
        }

        return DriverResponseDTO.builder()
                .id(d.getId())
                .name(d.getName())
                .email(d.getEmail())
                .phone(d.getPhone())
                .vehicleNumber(d.getVehicleNumber())
                .location(loc)
                .isOnline(d.getIsOnline())
                .rating(d.getRating())
                .createdAt(d.getCreatedAt())
                .build();
    }
}
