package com.baniya.cart.microservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MerchantDTO {

    private MerchantDetailsDTO merchantDetails;
    private String productId;
    private int stock;
    private double price;
}
