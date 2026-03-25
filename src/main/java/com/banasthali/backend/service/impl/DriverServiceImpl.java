package com.banasthali.backend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banasthali.backend.dto.AuthResponse;
import com.banasthali.backend.dto.driver.DriverLocationUpdateRequest;
import com.banasthali.backend.dto.driver.DriverLoginRequest;
import com.banasthali.backend.dto.driver.DriverRegisterRequest;
import com.banasthali.backend.dto.driver.DriverResponseDTO;
import com.banasthali.backend.model.Booking;
import com.banasthali.backend.model.Role;
import com.banasthali.backend.model.User;
import com.banasthali.backend.repository.UserRepository;
import com.banasthali.backend.service.DriverService;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private UserRepository userRepository;



    // LOGIN DRIVER
    @Override
    public AuthResponse loginDriver(DriverLoginRequest request){

        User user = userRepository

                .findByEmail(request.getEmail())

                .orElseThrow(() ->
                        new RuntimeException("Driver not found"));


        if(!user.getRole().equals(Role.DRIVER)){

            throw new RuntimeException("User is not DRIVER");

        }

        AuthResponse response = new AuthResponse();

        response.setEmail(user.getEmail());

        response.setRole("DRIVER");

        response.setMessage("Login successful");

        return response;

    }



    // DRIVER PROFILE
    @Override
    public DriverResponseDTO getDriverProfile(String email){

        User user = userRepository

                .findByEmail(email)

                .orElseThrow(() ->
                        new RuntimeException("Driver not found"));


        if(!user.getRole().equals(Role.DRIVER)){

            throw new RuntimeException("User is not DRIVER");

        }

        return mapToDTO(user);

    }



    // ONLINE STATUS
    @Override
    public DriverResponseDTO updateOnlineStatus(String email, boolean online){

        User user = userRepository

                .findByEmail(email)

                .orElseThrow(() ->
                        new RuntimeException("Driver not found"));


        user.setDriverAvailable(online);

        userRepository.save(user);

        return mapToDTO(user);

    }



    // UPDATE LOCATION
    @Override
    public DriverResponseDTO updateLocation(

            String email,

            DriverLocationUpdateRequest request){

        User user = userRepository

                .findByEmail(email)

                .orElseThrow(() ->
                        new RuntimeException("Driver not found"));


        user.setLatitude(request.getLatitude());

        user.setLongitude(request.getLongitude());

        userRepository.save(user);

        return mapToDTO(user);

    }



    // RIDE HISTORY (optional)
    @Override
    public List<Booking> getRideHistory(String email){

        return List.of();

    }



    // REGISTER DRIVER
    @Override
    public AuthResponse registerDriver(

            DriverRegisterRequest request){

        User user = User.builder()

                .username(request.getName())

                .email(request.getEmail())

                .password(request.getPassword())

                .role(Role.DRIVER)

                .driverAvailable(false)

                .build();


        userRepository.save(user);


        AuthResponse response = new AuthResponse();

        response.setEmail(user.getEmail());

        response.setRole("DRIVER");

        response.setMessage("Driver registered");

        return response;

    }



    // convert User → DriverResponseDTO
    private DriverResponseDTO mapToDTO(User user){

        DriverResponseDTO dto = new DriverResponseDTO();

        dto.setId(user.getId());

        dto.setName(user.getUsername());

        dto.setEmail(user.getEmail());

        return dto;

    }

}