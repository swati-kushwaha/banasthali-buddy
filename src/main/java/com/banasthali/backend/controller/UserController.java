package com.banasthali.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
}
