package com.banasthali.backend.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.banasthali.backend.model.Driver;
import com.banasthali.backend.model.User;
import com.banasthali.backend.repository.DriverRepository;
import com.banasthali.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Try loading application user first, then driver
        return userRepository.findByEmail(email)
            .map(u -> (UserDetails) u)
            .orElseGet(() -> driverRepository.findByEmail(email)
                .map(d -> (UserDetails) d)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email)));
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }

    public Driver getDriverById(String id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Driver not found with id: " + id));
    }
}
