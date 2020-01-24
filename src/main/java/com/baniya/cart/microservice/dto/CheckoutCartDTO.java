package com.baniya.cart.microservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CheckoutCartDTO {

    boolean success;
    List<String> productId;
}
