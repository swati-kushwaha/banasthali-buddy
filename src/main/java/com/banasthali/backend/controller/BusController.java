package com.banasthali.backend.controller;

import com.banasthali.backend.model.Post;
import com.banasthali.backend.model.User;
import com.banasthali.backend.repository.PostRepository;
import com.banasthali.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/bus")
@RequiredArgsConstructor
public class BusController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;


    @GetMapping("/eta/{postId}")
    public List<Map<String,Object>> getBusETA(

            @PathVariable String postId
    ){

        Post stop = postRepository
                .findById(postId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Post not found with id: " + postId
                        ));


        List<User> drivers =
                userRepository
                        .findByRoleAndDriverAvailableTrue(
                                "DRIVER"
                        );


        List<Map<String,Object>> result =
                new ArrayList<>();


        for(User d : drivers){

            if(d.getLatitude() == null)
                continue;


            double eta =
                    calculateETA(

                            d.getLatitude(),
                            d.getLongitude(),

                            stop.getLatitude(),
                            stop.getLongitude()

                    );


            Map<String,Object> bus =
                    new HashMap<>();


            bus.put("driverId", d.getId());

            bus.put("etaMinutes", eta);

            bus.put("latitude", d.getLatitude());

            bus.put("longitude", d.getLongitude());


            result.add(bus);

        }


        return result;

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



        double c = 2 * Math.atan2(

                Math.sqrt(a),

                Math.sqrt(1-a)

        );


        double distance = R * c;


        double avgSpeed = 25;


        return (distance / avgSpeed) * 60;

    }
    //update driver location
    @PostMapping("/update-location")
    public Map<String,String> updateDriverLocation(

            @RequestBody Map<String,Object> request
    ){

        String driverId =
                (String) request.get("driverId");

        Double latitude =
                Double.valueOf(
                        request.get("latitude").toString()
                );

        Double longitude =
                Double.valueOf(
                        request.get("longitude").toString()
                );


        User driver =
                userRepository
                        .findById(driverId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Driver not found"
                                )
                        );


        driver.setLatitude(latitude);

        driver.setLongitude(longitude);

        driver.setDriverAvailable(true);

        userRepository.save(driver);


        Map<String,String> response =
                new HashMap<>();

        response.put("message",
                "location updated");

        return response;

    }
    //fetch driver location
    @GetMapping("/location/{driverId}")
    public Map<String,Object> getDriverLocation(

            @PathVariable String driverId
    ){

        User driver =
                userRepository
                        .findById(driverId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Driver not found"
                                )
                        );


        Map<String,Object> response =
                new HashMap<>();


        response.put("driverId",
                driver.getId());

        response.put("latitude",
                driver.getLatitude());

        response.put("longitude",
                driver.getLongitude());

        response.put("isOnline",
                driver.getDriverAvailable());


        return response;

    }

}