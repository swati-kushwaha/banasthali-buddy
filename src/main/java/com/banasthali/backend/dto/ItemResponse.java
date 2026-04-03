package com.banasthali.backend.dto;

import com.banasthali.backend.model.Item;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemResponse {

    private String id;
    private String title;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String category;
    private String sellerId;
    private String sellerName;
    private boolean available;
    private String sellerPhone;
    private String sellerHostel;
    private String sellerRoom;
    private LocalDateTime createdAt;

    public static ItemResponse fromItem(Item item) {

        return ItemResponse.builder()

                .id(item.getId())

                .title(item.getTitle())

                .description(item.getDescription())

                .price(item.getPrice())

                .imageUrl(item.getImageUrl())

                .category(item.getCategory())

                .sellerId(item.getSellerId())

                .sellerName(item.getSellerName())

                .available(item.isAvailable())

                // ⭐ IMPORTANT FIX
                .sellerPhone(item.getSellerPhone())

                .sellerHostel(item.getSellerHostel())

                .sellerRoom(item.getSellerRoom())

                .createdAt(item.getCreatedAt())

                .build();
    }
}
