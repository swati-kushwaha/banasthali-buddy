package com.banasthali.backend.service;

import com.banasthali.backend.model.Booking;
import com.banasthali.backend.model.Post;
import com.banasthali.backend.model.User;
import com.banasthali.backend.repository.BookingRepository;
import com.banasthali.backend.repository.PostRepository;
import com.banasthali.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final SimpMessagingTemplate messagingTemplate;


    // FIXED METHOD
    public Booking requestBooking(String passengerId, String pickupPostId, String destinationPostId) {

        Booking booking = Booking.builder()
                .passengerId(passengerId)
                .pickupPostId(pickupPostId)
                .destinationPostId(destinationPostId)
                .status(Booking.BookingStatus.PENDING)
                .build();

        // IMPORTANT:
        // driverId intentionally null rahega
        // taaki pending ride sab drivers ko dikhe

        return bookingRepository.save(booking);
    }


    private double distanceBetweenPosts(Post a, Post b) {
        if (a == null || b == null || a.getLatitude() == null || b.getLatitude() == null) return Double.MAX_VALUE;
        double lat1 = a.getLatitude();
        double lon1 = a.getLongitude();
        double lat2 = b.getLatitude();
        double lon2 = b.getLongitude();
        double R = 6371e3;
        double phi1 = Math.toRadians(lat1);
        double phi2 = Math.toRadians(lat2);
        double deltaPhi = Math.toRadians(lat2 - lat1);
        double deltaLambda = Math.toRadians(lon2 - lon1);
        double sinDeltaPhi = Math.sin(deltaPhi / 2);
        double sinDeltaLambda = Math.sin(deltaLambda / 2);
        double aVal = sinDeltaPhi * sinDeltaPhi + Math.cos(phi1) * Math.cos(phi2) * sinDeltaLambda * sinDeltaLambda;
        double c = 2 * Math.atan2(Math.sqrt(aVal), Math.sqrt(1 - aVal));
        return R * c;
    }


    public Booking updateStatus(

            String bookingId,

            Booking.BookingStatus status,

            String driverId
    ){

        Booking booking = bookingRepository

                .findById(bookingId)

                .orElseThrow(() ->
                        new IllegalArgumentException("Booking not found"));


        if(booking.getDriverId() == null){

            booking.setDriverId(driverId);

        }


        if(!booking.getDriverId().equals(driverId)){

            throw new IllegalArgumentException(

                    "Not authorized to update booking"

            );

        }


        booking.setStatus(status);

        Booking saved = bookingRepository.save(booking);


        messagingTemplate.convertAndSend(

                "/topic/passenger/" + booking.getPassengerId(),

                saved

        );


        return saved;

    }


    private double calculateETA(

            double busLat,
            double busLng,
            double stopLat,
            double stopLng
    ){

        double R = 6371;

        double dLat = Math.toRadians(stopLat - busLat);
        double dLon = Math.toRadians(stopLng - busLng);

        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2)
                        +
                        Math.cos(Math.toRadians(busLat))
                                *
                                Math.cos(Math.toRadians(stopLat))
                                *
                                Math.sin(dLon/2)
                                *
                                Math.sin(dLon/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double distance = R * c;


        double avgBusSpeed = 25.0;


        double timeHours = distance / avgBusSpeed;


        return timeHours * 60;
    }
}