package com.banasthali.backend.service;

import com.banasthali.backend.model.ERickshawBooking;
import com.banasthali.backend.repository.ERickshawBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ERickshawBookingServiceImpl implements ERickshawBookingService {

    private final ERickshawBookingRepository repository;

    @Autowired
    public ERickshawBookingServiceImpl(ERickshawBookingRepository repository) {
        this.repository = repository;
    }

    @Override
    public ERickshawBooking save(ERickshawBooking booking) {
        return repository.save(booking);
    }

    @Override
    public List<ERickshawBooking> findAll() {
        return repository.findAll();
    }
}
