package com.banasthali.backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Item {

    @Id
    private String id;

    @TextIndexed
    private String title;

    @TextIndexed
    private String description;

    private BigDecimal price;

    private String imageUrl;

    private String category;

    private String sellerId;

    private String sellerName;

    private boolean available = true;

    private LocalDateTime createdAt = LocalDateTime.now();
}
