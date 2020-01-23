package com.baniya.cart.microservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserProfile {

    private String id;
    private String name;
    private String email;
    private String imageUrl;
    private String provider;
    private String providerId;
}
