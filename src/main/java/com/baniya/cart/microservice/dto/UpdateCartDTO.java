package com.baniya.cart.microservice.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateCartDTO {

    private String productId;
    private String merchantId;
    private int counter;
}
