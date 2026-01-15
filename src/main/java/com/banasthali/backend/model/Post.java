package com.banasthali.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    private String id;
    private String name;
    private Double latitude;
    private Double longitude;
}
