package com.baniya.cart.microservice.dto;

import lombok.Data;

@Data
public class MerchantDetailsDTO {

    private String merchantId;
    private String merchantName;
    private String gstIN;
    private double merchantRating;
}
