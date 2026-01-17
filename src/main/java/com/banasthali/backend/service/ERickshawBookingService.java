package com.banasthali.backend.service;

import java.util.List;

import com.banasthali.backend.model.ERickshawBooking;

public interface ERickshawBookingService {

    ERickshawBooking save(ERickshawBooking booking);

    List<ERickshawBooking> findAll();

}
