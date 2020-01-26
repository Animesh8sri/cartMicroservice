package com.baniya.cart.microservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MerchantProduct {

    private String merchantId;
    private String productId;
    private int stock;
    private double price;
}
