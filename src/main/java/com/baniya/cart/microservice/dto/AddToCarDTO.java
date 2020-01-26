package com.baniya.cart.microservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCarDTO {

    private String productId;
    private String merchantId;
}
