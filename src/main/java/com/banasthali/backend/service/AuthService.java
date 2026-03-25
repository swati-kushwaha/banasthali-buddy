package com.banasthali.backend.service;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.banasthali.backend.dto.AuthResponse;
import com.banasthali.backend.dto.LoginRequest;
import com.banasthali.backend.dto.RegisterRequest;
import com.banasthali.backend.model.Role;
import com.banasthali.backend.model.User;
import com.banasthali.backend.repository.UserRepository;
import com.banasthali.backend.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;


    // SIGNUP
    public AuthResponse register(RegisterRequest request) {

        // check email exists
        if (userRepository.existsByEmail(request.getEmail())) {

            return AuthResponse.message("Email already registered");

        }

        // create user with default STUDENT role
        User user = User.builder()

                .username(request.getUsername())

                .email(request.getEmail())

                .password(passwordEncoder.encode(request.getPassword()))

                .role(Role.STUDENT)   // default role

                .build();


        try {

            User savedUser = userRepository.save(user);


            String token = jwtTokenProvider.generateToken(

                    savedUser.getEmail(),

                    savedUser.getId(),

                    savedUser.getRole().name()

            );


            return AuthResponse.success(

                    token,

                    savedUser.getId(),

                    savedUser.getUsername(),

                    savedUser.getEmail(),

                    savedUser.getRole().name()

            );


        } catch (DuplicateKeyException e) {

            log.warn("Duplicate email: {}", request.getEmail());

            return AuthResponse.message("Email already registered");

        } catch (Exception e) {

            log.error("Signup error", e);

            return AuthResponse.message("Error creating user");

        }

    }


    // LOGIN
    public AuthResponse login(LoginRequest request) {

        try {

            authenticationManager.authenticate(

                    new UsernamePasswordAuthenticationToken(

                            request.getEmail(),

                            request.getPassword()

                    )

            );

        } catch (AuthenticationException e) {

            return AuthResponse.message("Invalid email or password");

        }


        User user = userRepository

                .findByEmail(request.getEmail())

                .orElse(null);


        if (user == null) {

            return AuthResponse.message("User not found");

        }


        String token = jwtTokenProvider.generateToken(

                user.getEmail(),

                user.getId(),

                user.getRole().name()

        );


        return AuthResponse.success(

                token,

                user.getId(),

                user.getUsername(),

                user.getEmail(),

                user.getRole().name()

        );

    }

}