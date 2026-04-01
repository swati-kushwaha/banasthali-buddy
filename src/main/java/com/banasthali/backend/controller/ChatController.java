package com.banasthali.backend.controller;

import com.banasthali.backend.model.Message;
import com.banasthali.backend.repository.MessageRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chat")

@RequiredArgsConstructor

public class ChatController {

    private final MessageRepository repo;

    // send message

    @PostMapping("/send")

    public Message sendMessage(
            @RequestBody Message msg
    ){

        msg.setTimestamp(LocalDateTime.now());

        return repo.save(msg);
    }

    // get chat messages

    @GetMapping("/{bookingId}")

    public List<Message> getMessages(

            @PathVariable String bookingId
    ){

        return repo.findByBookingIdOrderByTimestampAsc(
                bookingId
        );
    }
}