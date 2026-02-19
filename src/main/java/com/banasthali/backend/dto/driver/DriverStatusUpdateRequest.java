package com.banasthali.backend.dto.driver;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DriverStatusUpdateRequest {
    @NotNull
    private Boolean online;
}
