package com.banasthali.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "messages")

public class Message {

    @Id
    private String id;

    private String bookingId;

    private String senderId;

    private String message;

    private LocalDateTime timestamp;
}