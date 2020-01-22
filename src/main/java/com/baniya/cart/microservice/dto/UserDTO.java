package com.baniya.cart.microservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.AuthProvider;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private String id;
    private String name;
    private String email;
    private String imageUrl;
    private String provider;
    private String providerId;
}
