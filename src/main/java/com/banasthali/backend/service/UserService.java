package com.banasthali.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.banasthali.backend.model.User;
import com.banasthali.backend.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        return userRepository.save(user);
    }
    public User getProfile(User user){

        return userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

    }
    public User updateProfile(User user, String username){

        user.setUsername(username);

        return userRepository.save(user);

    }
    public String changePassword(User user, String newPassword){

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        return "Password updated";

    }

    public User updateDriverAvailability(User user, Boolean available){

        user.setDriverAvailable(available);

        return userRepository.save(user);

    }
}
