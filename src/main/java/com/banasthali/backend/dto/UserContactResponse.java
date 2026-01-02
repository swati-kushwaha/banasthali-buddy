package com.banasthali.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserContactResponse {

    private String id;
    private String username;
    private String email;
}
