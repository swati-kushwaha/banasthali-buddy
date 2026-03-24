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
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    // users
    @GetMapping("/users")
    public List<User> getAllUsers(){
        return adminService.getAllUsers();
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable String id){
        adminService.deleteUser(id);
        return "User deleted";
    }

    // items
    @GetMapping("/items")
    public List<Item> getAllItems(){
        return adminService.getAllItems();
    }

    @DeleteMapping("/item/{id}")
    public String deleteItem(@PathVariable String id){
        adminService.deleteItem(id);
        return "Item deleted";
    }

    // dashboard stats
    @GetMapping("/stats")
    public Map<String,Long> stats(){
        Map<String,Long> data = new HashMap<>();

        data.put("totalUsers",adminService.totalUsers());
        data.put("totalItems",adminService.totalItems());

        return data;
    }
}