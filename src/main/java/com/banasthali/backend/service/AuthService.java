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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    private final JavaMailSender mailSender;


    // SIGNUP
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {

            return AuthResponse.message("Email already registered");

        }

        User user = User.builder()

                .username(request.getUsername())

                .email(request.getEmail())

                .password(passwordEncoder.encode(request.getPassword()))

                .role(Role.STUDENT)

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


    /*
    ======================================
    FORGOT PASSWORD LOGIC (ADDED)
    ======================================
    */

    public void processForgotPassword(String email) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()){

            throw new IllegalArgumentException("Email not found");

        }

        User user = optionalUser.get();

        String token = UUID.randomUUID().toString();

        user.setResetToken(token);

        user.setResetTokenExpiry(

                LocalDateTime.now().plusMinutes(30)

        );

        userRepository.save(user);


        String resetLink =

                "https://banasthali-buddy.onrender.com/reset-password?token=" + token;


        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);

        message.setSubject("Reset Password");

        message.setText(

                "Click the link to reset your password:\n" + resetLink +

                        "\n\nLink valid for 30 minutes."

        );


        mailSender.send(message);

    }



    public void resetPassword(String token, String newPassword){

        Optional<User> optionalUser =

                userRepository.findByResetToken(token);


        if(optionalUser.isEmpty()){

            throw new IllegalArgumentException("Invalid token");

        }

        User user = optionalUser.get();


        if(

                user.getResetTokenExpiry()

                        .isBefore(LocalDateTime.now())

        ){

            throw new IllegalArgumentException("Token expired");

        }


        user.setPassword(

                passwordEncoder.encode(newPassword)

        );

        user.setResetToken(null);

        user.setResetTokenExpiry(null);

        userRepository.save(user);

    }

}