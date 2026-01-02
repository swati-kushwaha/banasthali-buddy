package com.banasthali.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Student Exchange Hub API",
        version = "1.0",
        description = "REST API for the Student Exchange Hub - A marketplace for students to buy and sell items",
        contact = @Contact(
            name = "Banasthali Buddy",
            email = "support@banasthali.edu"
        )
    )
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "JWT authentication. Get token from /api/auth/login endpoint."
)
public class OpenApiConfig {
}
