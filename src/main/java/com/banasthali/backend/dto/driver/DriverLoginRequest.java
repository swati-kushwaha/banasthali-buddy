package com.banasthali.backend.dto.driver;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DriverLoginRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
