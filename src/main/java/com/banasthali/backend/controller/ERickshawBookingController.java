package com.banasthali.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banasthali.backend.model.ERickshawBooking;
import com.banasthali.backend.service.ERickshawBookingService;

@RestController
@RequestMapping("/api/erickshaw")
@CrossOrigin(origins = "*")
public class ERickshawBookingController {

    private final ERickshawBookingService service;

    @Autowired
    public ERickshawBookingController(ERickshawBookingService service) {
        this.service = service;
    }

    @PostMapping("/book")
    public ResponseEntity<ERickshawBooking> book(@RequestBody ERickshawBooking booking) {
        ERickshawBooking saved = service.save(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ERickshawBooking>> getAll() {
        List<ERickshawBooking> all = service.findAll();
        return ResponseEntity.ok(all);
    }
}
 