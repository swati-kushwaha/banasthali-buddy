package com.banasthali.backend.controller;

import com.banasthali.backend.dto.UserContactResponse;
import com.banasthali.backend.model.User;
import com.banasthali.backend.service.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User profile and contact endpoints")
@RequiredArgsConstructor
public class UserProfileController {

    private final CustomUserDetailsService userDetailsService;

    @GetMapping("/{sellerId}")
    @Operation(summary = "Get seller contact details", description = "Retrieves contact information for a seller to facilitate communication")
    public ResponseEntity<?> getSellerContact(@PathVariable String sellerId) {
        try {
            User user = userDetailsService.getUserById(sellerId);
            UserContactResponse response = new UserContactResponse(
                user.getId(),
                user.getDisplayName(),
                user.getEmail()
            );
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
