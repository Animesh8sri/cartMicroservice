package com.baniya.cart.microservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MerchantViewCustomerDTO {

    private String merchantId;
    private String productName;
    private String timestamp;
    private int counter;
    private Double price;
}
