package com.banasthali.backend.controller;

import com.banasthali.backend.dto.ChangePasswordRequest;
import com.banasthali.backend.dto.DriverAvailabilityRequest;
import com.banasthali.backend.dto.UpdateProfileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.banasthali.backend.model.User;
import com.banasthali.backend.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public User addUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public User getProfile(
            @AuthenticationPrincipal User user){

        return userService.getProfile(user);

    }
    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public User updateProfile(

            @AuthenticationPrincipal User user,

            @RequestBody UpdateProfileRequest request
    ){

        return userService.updateProfile(

                user,
                request.getUsername()

        );

    }
    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public String changePassword(

            @AuthenticationPrincipal User user,

            @RequestBody ChangePasswordRequest request
    ){

        return userService.changePassword(

                user,
                request.getPassword()

        );

    }
    @PatchMapping("/driver/availability")
    @PreAuthorize("hasRole('DRIVER')")
    public User updateAvailability(

            @AuthenticationPrincipal User user,

            @RequestBody DriverAvailabilityRequest request
    ){

        return userService.updateDriverAvailability(

                user,
                request.getAvailable()

        );

    }

}
