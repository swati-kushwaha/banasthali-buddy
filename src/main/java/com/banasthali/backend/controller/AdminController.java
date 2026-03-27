package com.banasthali.backend.controller;

import com.banasthali.backend.model.Item;
import com.banasthali.backend.model.User;
import com.banasthali.backend.service.AdminService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")

public class AdminController {

    private final AdminService adminService;


    /// USERS
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers(){

        return adminService.getAllUsers();

    }


    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(

            @PathVariable String id

    ){

        adminService.deleteUser(id);

        return "User deleted successfully";

    }



    /// ITEMS
    @GetMapping("/items")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Item> getAllItems(){

        return adminService.getAllItems();

    }


    @DeleteMapping("/item/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteItem(

            @PathVariable String id

    ){

        adminService.deleteItem(id);

        return "Item deleted successfully";

    }



    /// DASHBOARD STATS
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Long> stats(){

        Map<String, Long> data = new HashMap<>();

        data.put(
                "totalUsers",
                adminService.totalUsers()
        );

        data.put(
                "totalItems",
                adminService.totalItems()
        );

        return data;

    }

}