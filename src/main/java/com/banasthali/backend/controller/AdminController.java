package com.banasthali.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.banasthali.backend.model.User;
import com.banasthali.backend.repository.UserRepository;
import com.banasthali.backend.repository.DriverRepository;
import com.banasthali.backend.repository.ERickshawBookingRepository;
import com.banasthali.backend.repository.ItemRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private ERickshawBookingRepository eRickshawBookingRepository;

    @Autowired
    private ItemRepository itemRepository;


    // ================================
    // Admin Dashboard API
    // ================================
    @GetMapping("/dashboard")
    public Map<String, Long> getDashboardStats(){

        long students = userRepository.count();
        long drivers = driverRepository.count();
        long activeRides = eRickshawBookingRepository.count();
        long listings = itemRepository.count();

        Map<String, Long> stats = new HashMap<>();

        stats.put("students", students);
        stats.put("drivers", drivers);
        stats.put("activeRides", activeRides);
        stats.put("listings", listings);

        return stats;
    }


    // ================================
    // Get All Users
    // ================================
    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


    // ================================
    // Delete User
    // ================================
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable String id){

        userRepository.deleteById(id);

        return "User deleted successfully";
    }

}